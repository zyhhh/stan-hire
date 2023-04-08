package cn.stan.common.exception;

import cn.stan.common.result.ResponseStatusEnum;

public class CustomException extends RuntimeException {

    private ResponseStatusEnum responseStatusEnum;

    public CustomException(ResponseStatusEnum responseStatusEnum) {
        super("自定义异常，状态码：" + responseStatusEnum.status()
                + "，报错信息：" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }
}
