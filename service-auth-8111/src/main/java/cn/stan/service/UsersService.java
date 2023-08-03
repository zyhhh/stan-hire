package cn.stan.service;

import cn.stan.pojo.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
public interface UsersService {

    /**
     * 根据电话查询用户
     * @param mobile
     * @return
     */
    Users queryUserByMobile(String mobile);

    /**
     * 创建用户
     * @param mobile
     * @return
     */
    Users createUsers(String mobile);

    /**
     * 根据主键id查询用户
     * @param userId
     * @return
     */
    Users getById(String userId);
}
