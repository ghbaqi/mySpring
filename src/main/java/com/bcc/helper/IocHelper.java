package com.bcc.helper;

import com.bcc.annotation.Inject;
import com.bcc.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

/**
 * IOC 实现依赖注入
 * 遍历 beanMap , 若某个成员变量带有 inject 注解, 再从 map 中取出对应的bean ,为他赋值
 * eg.  Controller 中注入 service
 */
public final class IocHelper {
    private IocHelper() {
    }

    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (!beanMap.isEmpty()) {
            beanMap.entrySet().stream().forEach(entry -> {
                Class<?> clazz = entry.getKey();
                Object instance = entry.getValue();
                Field[] fields = clazz.getDeclaredFields();

                // 对 bean 中成员变量带有 Inject 注解的类 ， 进行赋值
                Arrays.stream(fields).filter(field -> field.isAnnotationPresent(Inject.class))
                        .forEach(field -> {
                            Class<?> type = field.getType();
                            Object fieldInstance = BeanHelper.getBean(type);  // TODO 可能为 null 么 ？
                            ReflectionUtil.setFiled(instance, field, fieldInstance);
                        });
            });
        }

    }
}
