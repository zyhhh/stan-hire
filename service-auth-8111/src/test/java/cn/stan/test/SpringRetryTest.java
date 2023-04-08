package cn.stan.test;

import cn.stan.api.retry.RetryComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SpringRetryTest {

    @Autowired
    private RetryComponent retryComponent;

    @Test
    public void retry() {
        boolean res = retryComponent.sendSMSWithRetry();
        log.info("最终结果为: res = {}", res);
    }
}
