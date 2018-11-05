package com.bcc.test;

import com.bcc.MainLoader;
import com.bcc.helper.BeanHelper;

import java.util.Map;

public class Test01 {

    public static void main(String[] args) {

        MainLoader.init();

        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        System.out.println(beanMap);
        TestController controller = BeanHelper.getBean(TestController.class);
        System.out.println(controller);
        System.out.println(controller.getTestService());
    }
}
