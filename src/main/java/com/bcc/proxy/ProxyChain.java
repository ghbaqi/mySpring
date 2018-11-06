package com.bcc.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProxyChain {

    private final Class<?>    targetClass;
    private final Object      targetObj;
    private final Method      targetMethod;
    private final MethodProxy methodProxy;
    private final Object[]    methodParams;
    private List<Proxy> proxyList = new ArrayList<>();
    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObj, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObj = targetObj;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTargetObj() {
        return targetObj;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    /**
     *  执行代理链
     */
    public Object doProxyChain() throws Throwable {
        Object result;
        if (proxyIndex < proxyList.size()) {
            result = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            result  = methodProxy.invokeSuper(targetObj,methodParams);
        }
        return result;
    }
}
