package cn.stan.service;

import cn.stan.common.result.PagedGridResult;
import cn.stan.pojo.Admin;
import cn.stan.pojo.bo.AdminBO;
import cn.stan.pojo.bo.AdminCreateBO;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务类
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
public interface AdminService {

    /**
     * 创建admin账号
     * @param adminCreateBO
     */
    void createAdmin(AdminCreateBO adminCreateBO);

    /**
     * 查询admin列表
     * @param accountName
     * @param page
     * @param limit
     * @return
     */
    PagedGridResult getAdminList(String accountName, Integer page, Integer limit);

    /**
     * 删除admin账号
     * @param username
     */
    void deleteAdmin(String username);
}
