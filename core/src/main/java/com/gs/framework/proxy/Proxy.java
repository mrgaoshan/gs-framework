package com.gs.framework.proxy;

/**
 * 代理接口
 *
 * @author gao shan
 * @since 1.0.0
 */
public interface Proxy {

    /**
     * 执行链式代理
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}