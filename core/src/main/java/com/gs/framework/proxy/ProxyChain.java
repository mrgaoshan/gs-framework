package com.gs.framework.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理链
 *
 * @author gao shan
 * @since 1.0.0
 */
public class ProxyChain {

    private final Class<?> targetClass;//目标类
    private final Object targetObject;//目标对象
    private final Method targetMethod;//目标方法
    private final MethodProxy methodProxy;  //方法代理对象
    private final Object[] methodParams;//方法参数

    private List<Proxy> proxyList = new ArrayList<Proxy>();//代理列表
    private int proxyIndex = 0;   //代理对象的计数器

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    /**
     *　递归调用 doproxy，直到到达list 上线，返回 目标对象业务逻辑：View 对象
     */


    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {
            //获取proxy对象，调用doProxy
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            //
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);  //cglib 执行目标对象的业务逻辑，返回View 对象
        }
        return methodResult;
    }
}