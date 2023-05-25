package cn.stan.controller;

import cn.stan.api.mq.RabbitMQConfig;
import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.common.utils.GsonUtil;
import cn.stan.common.utils.IPUtil;
import cn.stan.common.utils.JWTUtil;
import cn.stan.common.utils.SMSUtil;
import cn.stan.pojo.Users;
import cn.stan.pojo.bo.RegistLoginBO;
import cn.stan.pojo.mq.SMSContentQO;
import cn.stan.pojo.vo.UsersVO;
import cn.stan.service.UsersService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("passport")
public class PassportController extends BaseInfoProperties {

    @Autowired
    private SMSUtil smsUtil;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 获取手机验证码
     * 60s内不能重复获取，详见 SMSInterceptor
     *
     * @param mobile
     * @param request
     * @return
     */
    @PostMapping("getSMSCode")
    public GraceResult getSMSCode(String mobile, HttpServletRequest request) {

        if (StringUtils.isBlank(mobile)) {
            return GraceResult.errorMsg("手机号为空");
        }
        // 获取用户ip地址，存入Redis，限制60s请求一次
        String ip = IPUtil.getRequestIp(request);
        redis.setnx60s(MOBILE_SMSCODE + ":" + ip, mobile);

        // 6位随机数
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        log.info("验证码: {}", code);

        // 过期时间，分钟
        int expireTime = 5;

        // 组装消息对象
        SMSContentQO contentQO = new SMSContentQO();
        contentQO.setMobile(mobile);
        contentQO.setContent(code);
        contentQO.setExpireTime(String.valueOf(expireTime));

        sendSmsWithMQ(contentQO);
        // smsUtil.sendSMS(mobile, code, String.valueOf(expireTime));

        // 将验证码存于Redis中
        redis.set(MOBILE_SMSCODE + ":" + mobile, code, expireTime * 60);

        return GraceResult.ok();
    }

    private void sendSmsWithMQ(SMSContentQO contentQO) {

        // 定义confirm回调，不管交换机是否成功收到消息都会进入
        // correlationData--相关性参数, ack--交换机是否成功收到消息, cause--失败原因
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("进入 confirmCallback >>>>");
            assert correlationData != null;
            log.info("correlationData: {}", correlationData.getId());
            if (ack) {
                // 成功时，cause为null
                log.info("交换机成功接收到消息，{}", cause);
            } else {
                log.info("交换机接收到消息失败，失败原因：{}", cause);
            }
        });

        // 定义return回调，消息没有正确路由到队列中则进入
        rabbitTemplate.setReturnsCallback(returnedMsg -> {
            log.info("进入 returnCallback >>>>");
            log.info("returnedMsg: {}", GsonUtil.objectToString(returnedMsg));
        });

        // 使用消息队列异步解耦发送短信
        rabbitTemplate.convertAndSend(RabbitMQConfig.SMS_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_SMS_SEND_LOGIN,
                GsonUtil.objectToString(contentQO),
                new CorrelationData());
    }

    /**
     * 注册/登录
     *
     * @param registLoginBO
     * @return
     */
    @PostMapping("login")
    public GraceResult login(@Valid @RequestBody RegistLoginBO registLoginBO) {

        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();

        // 1.校验验证码
        String code = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(code) || !smsCode.equalsIgnoreCase(code)) {
            return GraceResult.error(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 2.查询用户是否存在，存在则登录，不存在则注册
        Users user = usersService.queryUserByMobile(mobile);
        if (user == null) {
            user = usersService.createUsers(mobile);
        }

        /* 3.将token保存到redis中（有状态token）
        String uToken = TOKEN_USER_PREFIX + SYMBOL_DOT + UUID.randomUUID();
        redis.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken, 4 * 60 * 60);*/
        // 3.创建jwt，有效时间为60s（无状态token）
        // String jwt = jwtUtil.createJWTWithPrefix(new Gson().toJson(user), 60 * 1000L, TOKEN_USER_PREFIX);
        String jwt = jwtUtil.createJWTWithPrefix(new Gson().toJson(user), TOKEN_USER_PREFIX);

        // 4.删除redis中验证码
        redis.del(MOBILE_SMSCODE + ":" + mobile);

        // 5.组装数据返回给前端
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(jwt);

        return GraceResult.ok(userVO);
    }

    /**
     * 登出
     *
     * @param userId
     * @return
     */
    @PostMapping("logout")
    public GraceResult logout(@RequestParam("userId") String userId) {

        // 删除redis中的token
        redis.del(REDIS_USER_TOKEN + ":" + userId);

        return GraceResult.ok();
    }

}
