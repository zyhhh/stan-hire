package cn.stan.service.impl;

import cn.stan.common.utils.MD5Utils;
import cn.stan.mapper.AdminMapper;
import cn.stan.pojo.Admin;
import cn.stan.pojo.bo.AdminBO;
import cn.stan.service.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务实现类
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin getLoginAdmin(AdminBO adminBO) {

        // 根据用户名获取salt
        Admin adminDB = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", adminBO.getUsername()));

        if (Objects.isNull(adminDB)) {
            return null;
        }

        // 取出salt，比较密码
        String slat = adminDB.getSlat();
        String md5Str = MD5Utils.encrypt(adminBO.getPassword(), slat);
        if (Objects.equals(md5Str, adminDB.getPassword())) {
            return adminDB;
        }

        return null;
    }
}
