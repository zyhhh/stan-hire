package cn.stan.service;

import cn.stan.pojo.Admin;
import cn.stan.pojo.bo.AdminBO;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 获取admin登录信息，若登录信息无误返回对象，有误返回null
     * @param adminBO
     * @return null或admin信息
     */
    Admin getLoginAdmin(AdminBO adminBO);

}
