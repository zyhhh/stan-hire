package cn.stan.controller;

import cn.stan.common.result.GraceResult;
import cn.stan.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 简历相关
 */
@Slf4j
@RestController
@RequestMapping("resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * 用户注册时初始化简历
     *
     * @param userId
     * @return
     */
    @PostMapping("init")
    public GraceResult init(@RequestParam("userId") String userId) {
        resumeService.initResume(userId);
        return GraceResult.ok();
    }
}
