package com.bcc.helper;

import com.bcc.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * bean 容器类
 * 生成被框架管理的 bean
 * 放入 map 中，方便管理和使用
 */
public class BeanHelper {

    /**
     * 管理所有的 bean
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> c : beanClassSet) {
            Object o = ReflectionUtil.newInstance(c);
            BEAN_MAP.put(c, o);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取指定的  bean
     */
    public static <T> T getBean(Class<T> clazz) {
        Object o = BEAN_MAP.get(clazz);
        if (o == null) {
            throw new RuntimeException("未找到指定的 bean , clazz = " + clazz);
        }
        return (T) o;
    }
}
