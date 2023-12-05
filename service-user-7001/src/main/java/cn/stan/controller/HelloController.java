package cn.stan.controller;

import cn.stan.api.intercept.UserInfoInterceptor;
import cn.stan.common.result.GraceResult;
import cn.stan.pojo.Stu;
import cn.stan.pojo.Users;
import cn.stan.service.StuService;
import cn.stan.common.utils.SMSUtils;
import cn.stan.common.property.TencentCloudProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("u")
public class HelloController {

    @Autowired
    private StuService stuService;

    @Autowired
    private TencentCloudProperties properties;

    @Value("${server.port}")
    private String port;

    @Autowired
    private SMSUtils smsUtils;

    @GetMapping("stu")
    public GraceResult saveStu() {
        Stu stu = new Stu();
//        stu.setId("10001");
        stu.setName("张三");
        stu.setAge(23);
        stuService.save(stu);
        return GraceResult.ok(stu);
    }

    @GetMapping("hello")
    public Object hello() {
        Users users = UserInfoInterceptor.currentUser.get();
        log.info("当前登录用户信息: {}", users);
        return "hello, user:" + port;
    }

    @GetMapping("test")
    public Object test() {
        return properties.getSecretId() + "---" + properties.getSecretKey();
    }

    @GetMapping("testSMS")
    public GraceResult testSMS() {
        smsUtils.sendSMS("xxxx", "xxxx", "xxxx");
        return GraceResult.ok();
    }
}
