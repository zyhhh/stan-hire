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

    Users queryUserByMobile(String mobile);

    Users createUsers(String mobile);

}
