package cn.stan.controller;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.common.utils.JWTUtils;
import cn.stan.pojo.Users;
import cn.stan.pojo.vo.SaasUserVO;
import cn.stan.service.UsersService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("saas")
public class SaasPassportController extends BaseInfoProperties {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JWTUtils jwtUtils;

    /**
     * saas管理端获取二维码
     *
     * @return
     */
    @PostMapping("getQRToken")
    public GraceResult getQRToken() {

        // 生成扫码登陆的唯一token，并设置进Redis中，5分钟时效
        String qrToken = UUID.randomUUID().toString();

        redisUtils.set(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken, qrToken, 5 * 60L);

        redisUtils.set(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken, "0", 5 * 60L);

        return GraceResult.ok(qrToken);
    }

    /**
     * HR在saas管理端扫码登陆
     *
     * @param qrToken
     * @param request
     * @return
     */
    @PostMapping("scanCode")
    public GraceResult scanCode(String qrToken, HttpServletRequest request) {

        if (StringUtils.isBlank(qrToken))
            return GraceResult.error(ResponseStatusEnum.USER_PARAMS_ERROR);

        // 校验二维码令牌
        String redisQRToken = redisUtils.get(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken);
        if (!qrToken.equalsIgnoreCase(redisQRToken))
            return GraceResult.error(ResponseStatusEnum.USER_PARAMS_ERROR);

        // 检查用户请求头信息
        String headUserToken = request.getHeader("appUserToken");
        String headUserId = request.getHeader("appUserId");

        if (StringUtils.isBlank(headUserId) || StringUtils.isBlank(headUserToken))
            return GraceResult.error(ResponseStatusEnum.HR_TICKET_INVALID);

        // 校验jwt
        String userJson = jwtUtils.checkJWT(headUserToken.split(JWTUtils.AT)[1]);
        if (StringUtils.isBlank(userJson))
            return GraceResult.error(ResponseStatusEnum.HR_TICKET_INVALID);

        // 生成并返回预登录令牌
        String preToken = UUID.randomUUID().toString();
        redisUtils.set(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken, preToken, 5 * 60L);
        redisUtils.set(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken, "1," + preToken, 5 * 60L);

        return GraceResult.ok(preToken);
    }

    /**
     * saas管理端检测二维码是否被扫
     * 注：如果使用websocket或者netty，可以在app扫描之后，在上一个接口，直接通信浏览器（H5）进行页面扫码的状态标记
     *
     * @param qrToken
     * @return
     */
    @PostMapping("codeHasBeenRead")
    public GraceResult codeHasBeenRead(String qrToken) {

        String redisQRToken = redisUtils.get(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken);
        if (StringUtils.isBlank(redisQRToken)) {
            return GraceResult.ok(Collections.emptyList());
        }

        List<Object> list = new ArrayList<>();
        String[] split = redisQRToken.split(",");
        if (split.length >= 2) {
            list.add(Integer.parseInt(split[0]));
            list.add(split[1]);
        } else {
            list.add(0);
        }
        return GraceResult.ok(list);
    }

    /**
     * 手机端点击去登录，携带preToken与后端进行判断，如果校验ok则登录成功
     * 注：如果使用websocket或者netty，可以在此直接通信H5进行页面的跳转
     *
     * @param userId
     * @param qrToken
     * @param preToken
     * @return
     */
    @PostMapping("goQRLogin")
    public GraceResult goQRLogin(String userId,
                                 String qrToken,
                                 String preToken) {

        String preTokenRedisArr = redisUtils.get(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken);

        if (StringUtils.isBlank(preTokenRedisArr)) {
            return GraceResult.error(ResponseStatusEnum.FAILED);
        }

        String preTokenRedis = preTokenRedisArr.split(",")[1];
        if (preTokenRedis.equalsIgnoreCase(preToken)) {
            // 根据用户id获得用户信息
            Users hrUser = usersService.getById(userId);
            if (hrUser == null) {
                return GraceResult.error(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
            }

            // 存入用户信息到redis中，因为H5在未登录的情况下，拿不到用户id，所以暂存用户信息到redis。
            // ** 如果使用websocket是可以直接通信H5获得用户id，则无此问题
            redisUtils.set(REDIS_SAAS_USER_INFO + ":temp:" + preToken, new Gson().toJson(hrUser), 5 * 60L);
        }

        return GraceResult.ok();
    }

    /**
     * 页面登录跳转
     *
     * @param preToken
     * @return
     */
    @PostMapping("checkLogin")
    public GraceResult checkLogin(String preToken) {

        if (StringUtils.isBlank(preToken))
            return GraceResult.ok("");

        // 获得用户临时信息
        String userJson = redisUtils.get(REDIS_SAAS_USER_INFO + ":temp:" + preToken);

        if (StringUtils.isBlank(userJson))
            return GraceResult.error(ResponseStatusEnum.USER_NOT_EXIST_ERROR);

        // 确认执行登录后，生成saas用户的token，并且长期有效
        String saasUserToken = jwtUtils.createJWTWithPrefix(userJson, TOKEN_SAAS_PREFIX);

        // 存入用户信息，长期有效
        redisUtils.set(REDIS_SAAS_USER_INFO + ":" + saasUserToken, userJson);

        return GraceResult.ok(saasUserToken);
    }

    @GetMapping("info")
    public GraceResult info(String token) {

        String userJson = redisUtils.get(REDIS_SAAS_USER_INFO + ":" + token);
        Users saasUser = new Gson().fromJson(userJson, Users.class);

        SaasUserVO saasUserVO = new SaasUserVO();
        BeanUtils.copyProperties(saasUser, saasUserVO);

        return GraceResult.ok(saasUserVO);
    }

    @PostMapping("logout")
    public GraceResult logout(String token) {
        return GraceResult.ok();
    }

}