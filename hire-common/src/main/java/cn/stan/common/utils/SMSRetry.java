package cn.stan.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

@Slf4j
public class SMSRetry {

    public static boolean sendSMS() {

        int num = RandomUtils.nextInt(0, 6);
        log.info("随机数为: {}", num);

        switch (num) {
            case 0:
                return true;
            case 1:
                return false;
            case 2:
                throw new NullPointerException("空指针异常");
            case 3:
                throw new IllegalArgumentException("不合法参数异常");
            default:
                throw new IndexOutOfBoundsException("数组越界异常");
        }
    }

}


