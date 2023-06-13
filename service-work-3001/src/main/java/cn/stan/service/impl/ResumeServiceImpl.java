package cn.stan.service.impl;

import cn.stan.mapper.ResumeMapper;
import cn.stan.pojo.Resume;
import cn.stan.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stan
 * @since 2023-03-04
 */
@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initResume(String userId) {
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setCreateTime(LocalDateTime.now());
        resume.setUpdatedTime(LocalDateTime.now());
        resumeMapper.insert(resume);
    }
}
