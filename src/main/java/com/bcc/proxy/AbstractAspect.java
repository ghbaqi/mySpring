package com.bcc.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 抽象的切面类
 */
public abstract class AbstractAspect implements Proxy {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        Class<?> targetClass = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();
        begin();
        try {
            if (intercept(targetClass, targetMethod, params)) {
                before(targetClass, targetMethod, params);
                result = proxyChain.doProxyChain();
                after(targetClass, targetMethod, params, result);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Throwable e) {
            LOGGER.error("代理过程出错", e);
            error(targetClass, targetMethod, params, e);
            throw e;
        } finally {
            end();
        }


        return result;
    }

    /**
     *
     */
    public void end() {

    }

    public void error(Class<?> targetClass, Method targetMethod, Object[] params, Throwable e) {
    }

    public void after(Class<?> targetClass, Method targetMethod, Object[] params, Object result) {
    }

    public void before(Class<?> targetClass, Method targetMethod, Object[] params) {
    }

    public boolean intercept(Class<?> targetClass, Method targetMethod, Object[] params) {
        return true;
    }

    public void begin() {
    }
}
