package com.bcc.web;

/**
 * 返回数据
 */
public class ServerResponse {

    private Object model;

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public ServerResponse(Object model) {
        this.model = model;
    }

    public ServerResponse() {
    }
}
