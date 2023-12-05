package cn.stan.api.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Component
@Aspect
public class ServiceLogAspect {

    @Pointcut("execution(* cn.stan.service.impl..*.*(..))")
    public void servicePointcut() {
    }

    /**
     * 记录所有service方法的执行时间
     * 【 * cn.stan.service.impl..*.*(..) 】表示：
     * 所有返回值 cn.stan.service.impl包下及子包下 所有类下 各种参数的所有方法
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("servicePointcut()")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        String methodStr = joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName();
        long execTime = stopWatch.getTotalTimeMillis();
        log.info("执行方法: {}, 执行时间: {}ms", methodStr, execTime);

        return proceed;
    }

}
