package com.bcc.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyManager {


    /**
     * 创建代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<?> targetClass, List<Proxy> proxyList) {
        return (T)Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,o,method,methodProxy,objects,proxyList).doProxyChain();
            }
        });
    }
}
