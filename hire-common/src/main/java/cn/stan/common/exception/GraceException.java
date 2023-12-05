package cn.stan.common.exception;

import cn.stan.common.result.RespStatusEnum;

public class GraceException {
    public static void display(RespStatusEnum statusEnum){
        // 抛出自定义异常，通过全局异常捕获
        throw new CustomException(statusEnum);
    }
}
