package cn.stan.api.intercept;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.exception.GraceException;
import cn.stan.common.result.RespStatusEnum;
import cn.stan.common.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该拦截器用于限制同一ip，60秒内只能请求一次短信验证码
 */
@Slf4j
public class SMSInterceptor extends BaseInfoProperties implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ip = IPUtils.getRequestIp(request);
        // 限制60s后才能再次获取验证码
        boolean exist = redisUtils.keyIsExist(MOBILE_SMSCODE + ":" + ip);

        if(exist){
            // 抛出异常
            GraceException.display(RespStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }

        return true;
    }
}
