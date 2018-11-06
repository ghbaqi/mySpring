package com.bcc.util;

public enum  MethodEunm {

    GET("GET"),POST("POST"),PUT("PUT"),DELETE("DELETE");

    private String method;

    MethodEunm(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
