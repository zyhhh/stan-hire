package cn.stan.mapper;

import cn.stan.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 Mapper 接口
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

}
