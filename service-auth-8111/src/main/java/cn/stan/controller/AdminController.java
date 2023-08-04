package cn.stan.controller;

import cn.stan.api.mq.RabbitMQConfig;
import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.common.utils.GsonUtils;
import cn.stan.common.utils.IPUtils;
import cn.stan.common.utils.JWTUtils;
import cn.stan.common.utils.SMSUtils;
import cn.stan.pojo.Admin;
import cn.stan.pojo.Users;
import cn.stan.pojo.bo.AdminBO;
import cn.stan.pojo.bo.RegistLoginBO;
import cn.stan.pojo.mq.SMSContentQO;
import cn.stan.pojo.vo.UsersVO;
import cn.stan.service.AdminService;
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
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("admin")
public class AdminController extends BaseInfoProperties {

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

        if(Objects.isNull(loginAdmin)){
            return GraceResult.error(ResponseStatusEnum.ADMIN_LOGIN_ERROR);
        }

        String adminToken = jwtUtils.createJWTWithPrefix(GsonUtils.objectToString(loginAdmin), TOKEN_ADMIN_PREFIX);

        return GraceResult.ok(adminToken);
    }

    /**
     * 登出
     *
     * @param userId
     * @return
     */
    @PostMapping("logout")
    public GraceResult logout(@RequestParam("userId") String userId) {

        return GraceResult.ok();
    }

}
