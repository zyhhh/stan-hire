package cn.stan.api.config;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
// @Component
// @Aspect
public class SeataTransactionAspect {
    // @Pointcut("execution(* cn.stan.service.impl..*.*(..))")
    public void servicePointcut() {
    }

    /**
     * 调用service方法前，手动加入或创建全局事务
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    // @Before("servicePointcut()")
    public void beginGlobalTransaction(JoinPoint joinPoint) throws TransactionException {
        log.info("开启全局事务");
        GlobalTransaction globalTransaction = GlobalTransactionContext.getCurrentOrCreate();
        globalTransaction.begin();
    }

    /**
     * 出现异常回滚全局事务
     *
     * @param throwable
     */
    // @AfterThrowing(
    //         pointcut = "servicePointcut()",
    //         throwing = "throwable"
    // )
    public void transactionRollback(Throwable throwable) throws TransactionException {
        log.info("捕获到异常，回滚全局事务，异常信息：{}", throwable.getMessage());
        String xid = RootContext.getXID();
        if (StringUtils.isNotBlank(xid)) {
            GlobalTransactionContext.reload(xid).rollback();
        }
    }

}
