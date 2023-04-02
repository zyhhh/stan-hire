package cn.stan.controller;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("saas")
public class SaaSPassportController extends BaseInfoProperties {

    @PostMapping("getQRToken")
    public GraceResult getQRToken() {

        // 生成扫码登陆的唯一token，并设置进Redis中，5分钟时效
        String qrToken = UUID.randomUUID().toString();

        redis.set(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken, qrToken, 5 * 60);

        redis.set(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken, "0", 5 * 60);

        return GraceResult.ok(qrToken);
    }


}
