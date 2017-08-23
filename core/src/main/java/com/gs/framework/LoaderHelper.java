package com.gs.framework;


import com.gs.framework.helper.BeanHelper;
import com.gs.framework.helper.ClassHelper;
import com.gs.framework.helper.ControllerHelper;
import com.gs.framework.helper.IocHelper;
import com.gs.framework.util.ClassUtil;

/**
 * 加载相应的 Helper 类
 *
 * @author gao shan
 * @since 1.0.0
 */
public final class LoaderHelper {

    public static void init() {
        Class<?>[] classList = {
            ClassHelper.class,
            BeanHelper.class,
            IocHelper.class,
            ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}