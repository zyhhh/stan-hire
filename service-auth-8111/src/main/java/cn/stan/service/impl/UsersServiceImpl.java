package cn.stan.service.impl;

import cn.stan.api.feign.WorkFeign;
import cn.stan.common.enums.Sex;
import cn.stan.common.enums.ShowWhichName;
import cn.stan.common.enums.UserRole;
import cn.stan.common.exception.GraceException;
import cn.stan.common.result.GraceResult;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.common.utils.DesensitizationUtils;
import cn.stan.common.utils.LocalDateUtils;
import cn.stan.mapper.UsersMapper;
import cn.stan.pojo.Users;
import cn.stan.service.UsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private WorkFeign workFeign;

    // 用户默认头像url
    private static final String USER_FACE = "https://hbimg.huabanimg.com/761e226fa727a6f986699afd8795deb41511e00b4d5e-dEBN7j_fw658";

    @Override
    public Users getById(String userId) {
        return usersMapper.selectById(userId);
    }

    @Override
    public Users queryUserByMobile(String mobile) {

        return usersMapper.selectOne(new QueryWrapper<Users>().eq("mobile", mobile));
    }

    // @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Users createUsers(String mobile) {

        Users user = new Users();

        user.setMobile(mobile);
        user.setNickname("用户" + DesensitizationUtils.commonDisplay(mobile));
        user.setRealName("用户" + DesensitizationUtils.commonDisplay(mobile));
        user.setShowWhichName(ShowWhichName.nickname.type);

        user.setSex(Sex.SECRET.type);
        user.setFace(USER_FACE);
        user.setEmail("");

        LocalDate birthday = LocalDateUtils.parseLocalDate("1980-01-01", LocalDateUtils.DATE_PATTERN);
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

        // 初始化简历
        workFeign.initResume(user.getId());
        // 解决workFeign方法报错被全局异常捕获，无法回滚问题
        /*GraceResult graceResult = workFeign.initResume(user.getId());
        if (graceResult.isFail()) {
            String xid = RootContext.getXID();
            if (StringUtils.isNotBlank(xid)) {
                try {
                    GlobalTransactionContext.reload(xid).rollback();
                } catch (TransactionException e) {
                    log.error(e.getMessage(), e);
                } finally {
                    GraceException.display(ResponseStatusEnum.USER_REGISTER_ERROR);
                }
            }
        }*/

        return user;
    }
}
