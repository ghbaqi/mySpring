package com.bcc.proxy;

public interface Proxy {

    /**
     * 执行代理
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
