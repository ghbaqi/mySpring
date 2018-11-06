package com.bcc.helper;

import com.bcc.annotation.Controller;
import com.bcc.annotation.Service;
import com.bcc.util.ClassUtil;

import java.lang.annotation.Annotation;
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

    /**
     * 获取某个类及其所有子类
     */
    public static Set<Class<?>> getClassesBySuper(Class<?> superClass) {
        return  CLASS_SET.stream().filter(c->superClass.isAssignableFrom(c)&&!superClass.equals(c))
                .collect(Collectors.toSet());
    }

    /**
     * 获取带有某注解的类
     * eg. 获取带有 @Aspect 注解的类
     */
    public static Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> anno) {
        return CLASS_SET.stream().filter(c->c.isAnnotationPresent(anno))
                .collect(Collectors.toSet());
    }
}
