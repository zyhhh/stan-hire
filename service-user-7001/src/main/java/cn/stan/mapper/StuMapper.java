package cn.stan.mapper;

import cn.stan.pojo.Stu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
@Repository
public interface StuMapper extends BaseMapper<Stu> {

}
