package cn.stan.common.exception;

import cn.stan.common.result.GraceResult;
import cn.stan.common.result.RespStatusEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获自定义异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public GraceResult handleCustomException(CustomException ex) {
        return GraceResult.error(ex.getRespStatusEnum());
    }

    /**
     * 捕获jwt解析相关异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({
            SignatureException.class,
            ExpiredJwtException.class,
            UnsupportedJwtException.class,
            MalformedJwtException.class,
            io.jsonwebtoken.security.SignatureException.class
    })
    public GraceResult handleJWTException(SignatureException ex) {
        log.error(ex.getMessage(), ex);
        return GraceResult.error(RespStatusEnum.JWT_SIGNATURE_ERROR);
    }

    /**
     * 捕获参数校验异常
     *
     * @param ex
     * @return
     */
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
