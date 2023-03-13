package cn.stan.service.impl;

import cn.stan.mapper.UsersMapper;
import cn.stan.pojo.Users;
import cn.stan.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
