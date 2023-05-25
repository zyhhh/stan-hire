package cn.stan.controller;

import cn.stan.common.result.GraceResult;
import cn.stan.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("init")
    public GraceResult init(@RequestParam("userId") String userId) {
        resumeService.initResume(userId);
        return GraceResult.ok();
    }
}
