package com.gs.demo1.aspect;

import com.gs.framework.annotation.Aspect;
import com.gs.framework.annotation.Controller;
import com.gs.framework.proxy.AspectProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;


@Aspect(Controller.class)
public class ControllerAspec2 extends AspectProxy

{

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.debug("---------- begin ControllerAspect2 ----------");

        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
        LOGGER.debug("----------- end  ControllerAspect2-----------");
    }
}
