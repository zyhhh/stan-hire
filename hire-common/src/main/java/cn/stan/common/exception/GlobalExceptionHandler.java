package cn.stan.common.exception;

import cn.stan.common.result.GraceResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public GraceResult handleCustomException(CustomException ex) {
        return GraceResult.exception(ex.getResponseStatusEnum());
    }
}
