package cn.stan.service.impl;

import cn.stan.common.exception.GraceException;
import cn.stan.common.result.RespStatusEnum;
import cn.stan.mapper.UsersMapper;
import cn.stan.pojo.Users;
import cn.stan.pojo.bo.ModifyUserBO;
import cn.stan.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
@Slf4j
@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Transactional
    @Override
    public void modifyUserInfo(ModifyUserBO modifyUserBO) {
        String userId = modifyUserBO.getUserId();
        if(StringUtils.isBlank(userId)){
            GraceException.display(RespStatusEnum.USER_INFO_UPDATED_ERROR);
        }

        Users users = new Users();
        BeanUtils.copyProperties(modifyUserBO, users);
        users.setId(userId);
        users.setUpdatedTime(LocalDateTime.now());

        usersMapper.updateById(users);
    }

    @Override
    public Users getById(String userId) {
        return usersMapper.selectById(userId);
    }
}
