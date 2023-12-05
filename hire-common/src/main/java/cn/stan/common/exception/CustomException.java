package cn.stan.common.exception;

import cn.stan.common.result.RespStatusEnum;

public class CustomException extends RuntimeException {

    private RespStatusEnum respStatusEnum;

    public CustomException(RespStatusEnum respStatusEnum) {
        super("自定义异常，状态码：" + respStatusEnum.status() + "，异常信息：" + respStatusEnum.msg());
        this.respStatusEnum = respStatusEnum;
    }

    public RespStatusEnum getRespStatusEnum() {
        return respStatusEnum;
    }

    public void setRespStatusEnum(RespStatusEnum respStatusEnum) {
        this.respStatusEnum = respStatusEnum;
    }
}
