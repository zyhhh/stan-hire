package cn.stan.controller;

import cn.stan.grace.result.GraceResult;
import cn.stan.pojo.Stu;
import cn.stan.service.StuService;
import cn.stan.utils.SMSUtils;
import cn.stan.utils.TencentCloudProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return "hello, user:" + port;
    }

    @GetMapping("test")
    public Object test() {
        return properties.getSecretId() + "---" + properties.getSecretKey();
    }

    @GetMapping("testSMS")
    public GraceResult testSMS() throws Exception {
        smsUtils.sendSMS("xxxx", "xxxx");
        return GraceResult.ok();
    }
}
