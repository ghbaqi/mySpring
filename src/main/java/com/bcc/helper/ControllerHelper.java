package com.bcc.helper;

import com.bcc.annotation.Action;
import com.bcc.web.Handler;
import com.bcc.web.Request;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 获取所有的 controller 。
 * 及 request 和 handler 之间的 一对一关系
 */
public final class ControllerHelper {

    private ControllerHelper() {
    }

    /**
     * request 和 handler 之间的 一对一关系
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

    static {
        Set<Class<?>> classSet = ClassHelper.getControllerClassSet();
        classSet.stream().forEach(c -> {
            Method[] methods = c.getDeclaredMethods();
            Arrays.stream(methods).filter(method -> method.isAnnotationPresent(Action.class)).forEach(method -> {
                Request request = new Request();
                String[] array = method.getDeclaredAnnotation(Action.class).value().split(":");
                request.setMethod(array[0].toLowerCase());
                request.setPath(array[1].startsWith("/") ? array[1] : "/" + array[1]);

                Handler handler = new Handler();
                handler.setController(c);
                handler.setActionMethod(method);
                if (ACTION_MAP.containsKey(request)) {
                    throw new RuntimeException("请求路径不能相同 request = "+request+" , handler = "+handler);
                }
                ACTION_MAP.put(request, handler);
            });
        });
    }

    public static Map<Request, Handler> getActionMap() {
        return ACTION_MAP;
    }
}
