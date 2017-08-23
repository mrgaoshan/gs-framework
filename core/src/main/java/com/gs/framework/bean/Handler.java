package com.gs.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装 RequestMapping 信息, 注解@RequestMapping对应的Controller 和 哪一个方法
 *
 * @author gao shan
 * @since 1.0.0
 */
public class Handler {

    /**
     * Controller 类
     */
    private Class<?> controllerClass;

    /**
     * Action 方法
     */
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
