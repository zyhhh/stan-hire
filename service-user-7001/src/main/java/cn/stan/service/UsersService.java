package cn.stan.service;

import cn.stan.pojo.Users;
import cn.stan.pojo.bo.ModifyUserBO;

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
     * 修改用户信息
     */
    void modifyUserInfo(ModifyUserBO modifyUserBO);

    /**
     * 根据主键id查询用户
     * @param userId
     * @return
     */
    Users getById(String userId);
}
