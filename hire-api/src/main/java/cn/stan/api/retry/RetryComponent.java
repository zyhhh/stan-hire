package cn.stan.api.retry;

import cn.stan.common.exception.GraceException;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.common.utils.SMSRetry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class RetryComponent {

    @Retryable(
            value = {
                    NullPointerException.class,
                    IllegalArgumentException.class
            }, // 拦截到指定的异常，进行重试，若不指定默认所有异常
            maxAttempts = 5, // 调用方法的总次数
            backoff = @Backoff(
                    delay = 1000L, // 重试的间隔时间为1s
                    multiplier = 2.0 // 每次重试间隔时间是之前的2倍，例：1/2/4/8
            )
    )
    public boolean sendSMSWithRetry() {
        log.info("当前时间: {}", LocalDateTime.now());
        return SMSRetry.sendSMS();
    }

    /**
     * 重试的兜底方法
     * 当达到最大重试次数，或者抛出的异常并非指定的异常，走此方法
     *
     * @return
     */
    @Recover
    public boolean recover() {
        GraceException.display(ResponseStatusEnum.FAILED);
        return false;
    }
}
