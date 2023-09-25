package cn.stan.api.intercept;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.utils.GsonUtils;
import cn.stan.pojo.Admin;
import cn.stan.pojo.Users;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该拦截器用于解析header中的用户信息，并设置进ThreadLocal中
 */
@Slf4j
public class UserInfoInterceptor extends BaseInfoProperties implements HandlerInterceptor {

    public static ThreadLocal<Users> currentUser = new ThreadLocal<>();
    public static ThreadLocal<Admin> currentAdmin = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 当前线程有且仅有一种用户登录 app、saas、admin

        // 普通用户：app、saas用户都是对应Users.class
        String userJson = request.getHeader(APP_USER_JSON);

        if (StringUtils.isBlank(userJson)) {
            userJson = request.getHeader(SAAS_USER_JSON);
        }

        if (StringUtils.isNotBlank(userJson)) {
            Users users = GsonUtils.stringToBean(userJson, Users.class);
            currentUser.set(users);
        }

        // 管理员：admin用户对应Admin.class
        String adminJson = request.getHeader(ADMIN_USER_JSON);

        if (StringUtils.isNotBlank(adminJson)) {
            Admin admin = GsonUtils.stringToBean(adminJson, Admin.class);
            currentAdmin.set(admin);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 移除ThreadLocal
        currentUser.remove();
        currentAdmin.remove();
    }
}
