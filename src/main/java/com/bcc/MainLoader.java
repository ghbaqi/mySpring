package com.bcc;

import com.bcc.helper.BeanHelper;
import com.bcc.helper.ClassHelper;
import com.bcc.helper.ConfigHelper;
import com.bcc.helper.IocHelper;
import com.bcc.util.ClassUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 初始化所有类
 * 注意加载的顺序
 */
public class MainLoader {

    public static void init() {
        List<Class<?>> classes = Arrays.asList(ConfigHelper.class,ClassHelper.class, BeanHelper.class, IocHelper.class);
        for (Class<?> aClass : classes) {
            ClassUtil.loadClass(aClass.getName());
        }
    }
}
