package com.bcc.helper;

import com.bcc.annotation.Controller;
import com.bcc.annotation.Service;
import com.bcc.util.ClassUtil;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 获取指定路径下的各种 类 Class
 */
public final class ClassHelper {

    private ClassHelper() {
    }

    /**
     * 存放应用项目包下的所有类
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取所有带 service 注解的类
     */
    public static Set<Class<?>> getServiceClassSet() {
        return CLASS_SET.stream().filter(c -> c.isAnnotationPresent(Service.class)).collect(Collectors.toSet());
    }

    /**
     * 获取所有带 controller 注解的类
     */
    public static Set<Class<?>> getControllerClassSet() {
        return CLASS_SET.stream().filter(c -> c.isAnnotationPresent(Controller.class)).collect(Collectors.toSet());
    }


    /**
     * 获取所有带注解的 bean  (service ， controller)
     * @return
     */
    public static Set<Class<?>> getBeanClassSet() {
        return CLASS_SET.stream().filter(c -> c.isAnnotationPresent(Controller.class) || c.isAnnotationPresent(Service.class)).collect(Collectors.toSet());
    }
}
