package com.bcc.web;

import com.bcc.util.CastUtil;

import java.util.Map;

/**
 * 请求参数
 */
public class RequestParam {

    private Map<String,Object> paramMap;

    public RequestParam(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public long getLong(String name) {
        return CastUtil.castInt(paramMap.get(name));
    }
}
