package com.bcc.proxy;

import com.bcc.annotation.Transaction;
import com.bcc.helper.DatabaseHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 事务切面类 , 类似于普通切面类 AbstractAspect
 */
public class TransactionAspect implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionAspect.class);

    private static ThreadLocal<Boolean> IS_EXECUTE = ThreadLocal.withInitial(() -> false);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object reult = null;
        Boolean flag = IS_EXECUTE.get();
        Method method = proxyChain.getTargetMethod();

        if (flag || !method.isAnnotationPresent(Transaction.class)) {
            return proxyChain.doProxyChain();
        }
        IS_EXECUTE.set(true);
        try {
            DatabaseHelper.beginTransaction();
            System.out.println("beginTransaction");
            reult = proxyChain.doProxyChain();
            DatabaseHelper.commitTransaction();
            System.out.println("commitTransaction");
        } catch (Exception e) {
            DatabaseHelper.rollBack();
            System.out.println("rollBack");
            throw new RuntimeException("事务执行过程出错", e);
        } finally {
            IS_EXECUTE.remove();
        }

        return reult;
    }
}
