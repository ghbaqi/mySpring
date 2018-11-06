package com.bcc.web;

import java.util.Map;

/**
 * handler 返回 view ，跳转页面
 */
public class ModelAndView {

    private String path;

    /**
     * 携带的数据
     */
    private Map<String,Object> model;

    public ModelAndView(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
