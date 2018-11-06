package com.bcc.test;

import com.bcc.MainLoader;
import com.bcc.helper.ControllerHelper;
import com.bcc.web.Handler;
import com.bcc.web.Request;

import java.util.Map;

public class Test01 {

    public static void main(String[] args) {

        MainLoader.init();

//        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
//        System.out.println(beanMap);
//        TestController controller = BeanHelper.getBean(TestController.class);
//        System.out.println(controller);
//        System.out.println(controller.getTestService());

        Map<Request, Handler> actionMap = ControllerHelper.getActionMap();
        System.out.println(actionMap.size());
        for (Map.Entry<Request, Handler> entry : actionMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}
