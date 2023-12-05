package cn.stan.controller;

import cn.stan.api.intercept.UserInfoInterceptor;
import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import cn.stan.common.result.RespStatusEnum;
import cn.stan.common.utils.GsonUtils;
import cn.stan.common.utils.JWTUtils;
import cn.stan.pojo.Admin;
import cn.stan.pojo.bo.AdminBO;
import cn.stan.pojo.vo.AdminVO;
import cn.stan.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("admin")
public class AdminLoginController extends BaseInfoProperties {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JWTUtils jwtUtils;

    /**
     * admin登录
     *
     * @param adminBO
     * @return
     */
    @PostMapping("login")
    public GraceResult login(@Valid @RequestBody AdminBO adminBO) {

        Admin loginAdmin = adminService.getLoginAdmin(adminBO);

        if (Objects.isNull(loginAdmin)) {
            return GraceResult.error(RespStatusEnum.ADMIN_LOGIN_ERROR);
        }

        String adminToken = jwtUtils.createToken(GsonUtils.objectToString(loginAdmin), TOKEN_ADMIN_PREFIX);

        return GraceResult.ok(adminToken);
    }

    /**
     * 获取登录admin信息
     *
     * @return
     */
    @GetMapping("info")
    public GraceResult currentAdminInfo() {
        // 从ThreadLocal中获取当前管理员
        Admin admin = UserInfoInterceptor.currentAdmin.get();

        AdminVO adminVO = new AdminVO();
        BeanUtils.copyProperties(admin, adminVO);

        return GraceResult.ok(adminVO);
    }

    /**
     * admin登出
     *
     * @return
     */
    @PostMapping("logout")
    public GraceResult logout() {
        return GraceResult.ok();
    }

}
