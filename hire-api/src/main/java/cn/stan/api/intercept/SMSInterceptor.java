package cn.stan.api.intercept;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.exception.GraceException;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.common.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SMSInterceptor extends BaseInfoProperties implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ip = IPUtil.getRequestIp(request);
        // 限制60s后才能再次获取验证码
        boolean exist = redis.keyIsExist(MOBILE_SMSCODE + ":" + ip);

        if(exist){
            // 抛出异常
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }

        return true;
    }
}
