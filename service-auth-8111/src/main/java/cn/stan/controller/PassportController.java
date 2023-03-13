package cn.stan.controller;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import cn.stan.common.utils.IPUtil;
import cn.stan.common.utils.SMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("passport")
public class PassportController extends BaseInfoProperties {

    @Autowired
    private SMSUtil smsUtil;

    @GetMapping("getSMSCode")
    public GraceResult getSMSCode(String phone, HttpServletRequest request) {
        if (StringUtils.isBlank(phone)) {
            return GraceResult.errorMsg("手机号为空");
        }
        // 获取用户ip地址，存入Redis，限制60s请求一次
        String ip = IPUtil.getRequestIp(request);
        redis.setnx60s(MOBILE_SMSCODE + ":" + ip, phone);

        // 6位随机数
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        log.info("验证码: {}", code);
        // 过期时间，分钟
        int expireTime = 30;
        // 发送短信
        // smsUtil.sendSMS(phone, code, String.valueOf(expireTime));
        // 将验证码存于Redis中
        redis.set(MOBILE_SMSCODE + ":" + phone, code, expireTime * 60);

        return GraceResult.ok();
    }

}
