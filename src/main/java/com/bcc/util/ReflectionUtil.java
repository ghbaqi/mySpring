package com.bcc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建对象
     */
    public static Object newInstance(Class<?> clazz) {
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            LOGGER.error("反射创建对象错误", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object target, Method method, Object... args) {
        Object result;
        method.setAccessible(true);
        try {
            result = method.invoke(target, args);
        } catch (Exception e) {
            LOGGER.error("反射调用方法错误", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置 filed 值
     */
    public static void setFiled(Object target, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            LOGGER.error("反射设置 filed 错误", e);
            throw new RuntimeException(e);
        }
    }
}
