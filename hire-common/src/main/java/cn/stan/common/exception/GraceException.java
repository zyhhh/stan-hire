package cn.stan.common.exception;

import cn.stan.common.result.ResponseStatusEnum;

public class GraceException {
    public static void display(ResponseStatusEnum statusEnum){
        // 抛出自定义异常，通过全局异常捕获
        throw new CustomException(statusEnum);
    }
}
