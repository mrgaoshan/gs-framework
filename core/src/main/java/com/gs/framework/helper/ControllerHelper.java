package com.gs.framework.helper;

import com.gs.framework.annotation.RequestMapping;
import com.gs.framework.bean.Handler;
import com.gs.framework.bean.Request;
import com.gs.framework.util.ArrayUtil;
import com.gs.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 *
 * @author gao shan
 * @since 1.0.0
 */
public final class ControllerHelper {

    //存放请求URL和 处理器 的映射关系（在哪一个controller和哪一个方法）
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();

        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            //遍历controller class
            for (Class<?> controllerClass : controllerClassSet) {
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        //带有@RequestMapping 注解的方法
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping action = method.getAnnotation(RequestMapping.class);
                            String mapping = action.value();
                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod, requestPath); //封装URL
                                    Handler handler = new Handler(controllerClass, method); //封装handler
                                    ACTION_MAP.put(request, handler); //放入MAP
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取 Handler
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
