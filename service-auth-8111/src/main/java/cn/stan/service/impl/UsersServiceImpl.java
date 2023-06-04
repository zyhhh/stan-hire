package cn.stan.service.impl;

import cn.stan.common.enums.Sex;
import cn.stan.common.enums.ShowWhichName;
import cn.stan.common.enums.UserRole;
import cn.stan.common.utils.DesensitizationUtil;
import cn.stan.common.utils.LocalDateUtil;
import cn.stan.mapper.UsersMapper;
import cn.stan.pojo.Users;
import cn.stan.service.UsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    private static final String USER_FACE = "https://hbimg.huabanimg.com/761e226fa727a6f986699afd8795deb41511e00b4d5e-dEBN7j_fw658";

    @Override
    public Users getById(String userId) {
        return usersMapper.selectById(userId);
    }

    @Override
    public Users queryUserByMobile(String mobile) {

        Users users = usersMapper.selectOne(new QueryWrapper<Users>()
                .eq("mobile", mobile));

        return users;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Users createUsers(String mobile) {

        Users user = new Users();

        user.setMobile(mobile);
        user.setNickname("用户" + DesensitizationUtil.commonDisplay(mobile));
        user.setRealName("用户" + DesensitizationUtil.commonDisplay(mobile));
        user.setShowWhichName(ShowWhichName.nickname.type);

        user.setSex(Sex.SECRET.type);
        user.setFace(USER_FACE);
        user.setEmail("");

        LocalDate birthday = LocalDateUtil.parseLocalDate("1980-01-01", LocalDateUtil.DATE_PATTERN);
        user.setBirthday(birthday);

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("这家伙很懒，什么都没留下~");

        // 参加工作的日期，默认使用注册当天的日期
        user.setStartWorkDate(LocalDate.now());
        user.setPosition("底层码农");
        user.setRole(UserRole.CANDIDATE.type);
        user.setHrInWhichCompanyId("");

        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        usersMapper.insert(user);

        return user;
    }
}
