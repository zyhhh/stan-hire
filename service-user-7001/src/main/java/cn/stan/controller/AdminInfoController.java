package cn.stan.controller;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import cn.stan.common.result.PagedGridResult;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.pojo.bo.AdminBO;
import cn.stan.pojo.bo.AdminCreateBO;
import cn.stan.pojo.bo.ResetPwdBO;
import cn.stan.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("admininfo")
public class AdminInfoController extends BaseInfoProperties {

    @Autowired
    private AdminService adminService;


    /**
     * 创建admin账号
     *
     * @param adminCreateBO
     * @return
     */
    @PostMapping("create")
    public GraceResult create(@Valid @RequestBody AdminCreateBO adminCreateBO) {
        adminService.createAdmin(adminCreateBO);
        return GraceResult.ok();
    }

    /**
     * 获取admin列表
     *
     * @param accountName
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("list")
    public GraceResult list(String accountName, Integer page, Integer limit) {
        if (page == null) page = 1;
        if (limit == null) limit = 10;

        PagedGridResult result = adminService.getAdminList(accountName, page, limit);
        return GraceResult.ok(result);
    }

    /**
     * 删除admin账号
     *
     * @param username
     * @return
     */
    @PostMapping("delete")
    public GraceResult delete(String username) {
        if (Objects.equals("admin", username)) {
            return GraceResult.error(ResponseStatusEnum.ADMIN_DELETE_ERROR);
        }
        adminService.deleteAdmin(username);
        return GraceResult.ok();
    }

    /**
     * 重置admin账号密码
     *
     * @param resetPwdBO
     * @return
     */
    @PostMapping("resetPwd")
    public GraceResult resetPwd(@RequestBody ResetPwdBO resetPwdBO) {

        // 此接口使用AR模式进行操作（active record）
        resetPwdBO.modifyPwd();
        return GraceResult.ok();
    }

}
