package cn.stan.common.exception;

import cn.stan.common.result.GraceResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public GraceResult handleCustomException(CustomException ex) {
        return GraceResult.error(ex.getResponseStatusEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GraceResult handleNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errorMap = getErrors(bindingResult);
        return GraceResult.errorMap(errorMap);
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        // key为错误对应的属性字段，value为错误信息
        Map<String, String> map = new HashMap<>();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return map;
    }
}
