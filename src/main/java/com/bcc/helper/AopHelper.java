package com.bcc.helper;

import com.bcc.annotation.Aspect;
import com.bcc.annotation.Service;
import com.bcc.proxy.AbstractAspect;
import com.bcc.proxy.Proxy;
import com.bcc.proxy.ProxyManager;
import com.bcc.proxy.TransactionAspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    private AopHelper() {

    }

    static {
        Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
        try {
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> entry : targetMap.entrySet()) {
                Class<?> targetClass = entry.getKey();
                List<Proxy> proxies = entry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxies);   // ! ! !
                BeanHelper.addBean(targetClass, proxy);                          //  ! ! ! 如何起作用的
                // 将原来 IOC 容器中的 Controller 对象, 已经替换成现在创建的代理对象
                // dispatchServlet  中获取到的是代理对象 , 是调用了代理对象的方法进行处理 .
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("aophelper 初始化错误", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取某个 Aspect 注解 所代理的所有类
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Set<Class<?>> classes = new HashSet<>();
        Class<? extends Annotation> anno = aspect.value();
        if (!anno.equals(Aspect.class)) {
            classes.addAll(ClassHelper.getClassesByAnnotation(anno));
        }
        return classes;
    }


    /**
     * 一个切面类 对应多个目标类
     * 1. 先获取所有 AbstractAspect 的子类 , 即先获取到所有的切面类
     * 2. 循环,获取每个切面类对应的所有 目标类 集合
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
        HashMap<Class<?>, Set<Class<?>>> map = new HashMap<>();
        addCommoAspectProxy(map);
        addTransactionAspectProxy(map);
        return map;
    }

    // TODO 暂时只支持 service 上的 事务注解
    /**
     * 将所有 service 类 都添加事务切面 TransactionAspect
     */
    private static void addTransactionAspectProxy(HashMap<Class<?>, Set<Class<?>>> map) {
        Set<Class<?>> classes = ClassHelper.getClassesByAnnotation(Service.class);
        map.put(TransactionAspect.class, classes);
    }

    /**
     * key   =  继承自 AbastractAspect  的所有普通切面类
     * value =  Aspect 的 value 值 所指向的所有类
     */
    private static void addCommoAspectProxy(HashMap<Class<?>, Set<Class<?>>> map) {
        Set<Class<?>> aspectClasses = ClassHelper.getClassesBySuper(AbstractAspect.class);
        aspectClasses.stream().filter(c -> c.isAnnotationPresent(Aspect.class))
                .forEach(c -> {
                    Aspect annotation = c.getAnnotation(Aspect.class);
                    Set<Class<?>> classesByAnnotation = ClassHelper.getClassesByAnnotation(annotation.value());
                    map.put(c, classesByAnnotation);
                });
    }


    /**
     * 一个目标类 对应 多个 切面类
     * 由上个方法得到的集合进行整理
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws IllegalAccessException, InstantiationException {

        // 目标类 和 代理类 一对多关系
        HashMap<Class<?>, List<Proxy>> map = new HashMap<>();

        Set<Map.Entry<Class<?>, Set<Class<?>>>> entries = proxyMap.entrySet();

        for (Map.Entry<Class<?>, Set<Class<?>>> entry : entries) {
            Class<?> proxyClass = entry.getKey();
            Set<Class<?>> aimClasses = entry.getValue();

            for (Class<?> aimClass : aimClasses) {
                if (map.containsKey(aimClass)) {
                    map.get(aimClass).add((Proxy) proxyClass.newInstance());
                } else {
                    ArrayList<Proxy> proxies = new ArrayList<>();
                    proxies.add((Proxy) proxyClass.newInstance());
                    map.put(aimClass, proxies);
                }
            }
        }
        return map;
    }

}
