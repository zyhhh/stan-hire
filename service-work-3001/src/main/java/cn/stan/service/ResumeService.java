package cn.stan.service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
public interface ResumeService {

    /**
     * 用户注册时初始化简历
     * @param userId
     */
    void initResume(String userId);

}
