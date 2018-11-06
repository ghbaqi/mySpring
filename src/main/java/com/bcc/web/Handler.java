package com.bcc.web;

import java.lang.reflect.Method;

/**
 * 处理器 : controller.action
 */
public class Handler {

    private Class<?> controller;

    private Method actionMethod;

    public Handler() {
    }

    public Handler(Class<?> controller, Method actionMethod) {
        this.controller = controller;
        this.actionMethod = actionMethod;
    }

    public Class<?> getController() {
        return controller;
    }

    public void setController(Class<?> controller) {
        this.controller = controller;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }

    @Override
    public String toString() {
        return "Handler{" +
                "controller=" + controller +
                ", actionMethod=" + actionMethod +
                '}';
    }
}
