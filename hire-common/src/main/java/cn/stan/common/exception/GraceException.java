package cn.stan.common.exception;

import cn.stan.common.result.ResponseStatusEnum;

public class GraceException {
    public static void display(ResponseStatusEnum statusEnum){
        throw new CustomException(statusEnum);
    }
}
