package cn.stan.service.impl;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.exception.GraceException;
import cn.stan.common.result.PagedGridResult;
import cn.stan.common.result.RespStatusEnum;
import cn.stan.common.utils.MD5Utils;
import cn.stan.mapper.AdminMapper;
import cn.stan.pojo.Admin;
import cn.stan.pojo.bo.AdminCreateBO;
import cn.stan.service.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
public class AdminServiceImpl extends BaseInfoProperties implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    // 管理员默认头像url
    private static final String ADMIN_FACE = "https://img0.baidu.com/it/u=1950040729,1776260727&fm=253&fmt=auto&app=138&f=JPEG?w=256&h=256";

    @Override
    public void createAdmin(AdminCreateBO adminCreateBO) {
        // 账号唯一性校验
        Admin adminDB = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", adminCreateBO.getUsername()));
        // 优雅的异常解耦处理
        if (Objects.nonNull(adminDB))
            GraceException.display(RespStatusEnum.ADMIN_USERNAME_EXIST_ERROR);

        Admin newAdmin = new Admin();
        BeanUtils.copyProperties(adminCreateBO, newAdmin);
        // 设置盐和密码
        String salt = (int) ((Math.random() * 9 + 1) * 100000) + "";
        String pwd = MD5Utils.encrypt(adminCreateBO.getPassword(), salt);
        newAdmin.setPassword(pwd);
        newAdmin.setSlat(salt);

        newAdmin.setFace(ADMIN_FACE);
        newAdmin.setCreateTime(LocalDateTime.now());
        newAdmin.setUpdatedTime(LocalDateTime.now());

        adminMapper.insert(newAdmin);
    }

    @Override
    public PagedGridResult getAdminList(String accountName, Integer page, Integer limit) {

        PageHelper.startPage(page, limit);

        List<Admin> adminList = adminMapper.selectList(new QueryWrapper<Admin>().like("username", accountName));

        return setterPagedGrid(adminList, page);
    }

    @Override
    public void deleteAdmin(String username) {

        int res = adminMapper.delete(new QueryWrapper<Admin>().eq("username", username));

        if (res == 0) GraceException.display(RespStatusEnum.ADMIN_DELETE_ERROR);
    }
}
