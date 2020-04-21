package com.park.common;

import java.util.HashMap;
import java.util.Map;

public class Func {

    /**
     * 一般的接口响应结构
     */
    private static Map<String, Object> resultMap(Boolean success, int statusCode, String message, Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", success);
        result.put("status", statusCode);
        result.put("msg", message);
        if (data != null) {
            String dataKey = success ? "result" : "error";
            result.put(dataKey, data);
        }
        return result;
    }

    public static Map<String, Object> successMap() {
        return resultMap(true, 200, "success", null);
    }

    public static Map<String, Object> successMap(String msg) {
        return resultMap(true, 200, msg, null);
    }

    public static Map<String, Object> successMap(Object data) {
        return resultMap(true, 200, "success", data);
    }

    public static Map<String, Object> successMap(String msg, Object data) {
        return resultMap(true, 200, msg, data);
    }

    public static Map<String, Object> failingMap(String msg) {
        return resultMap(false, 200, msg, null);
    }

    public static Map<String, Object> failingMap(String msg, Object data) {
        return resultMap(false, 200, msg, data);
    }

    public static Map<String, Object> failingMap(int statusCode, String msg, Object data) {
        return resultMap(false, statusCode, msg, data);
    }

    public static Map<String, Object> failingMap(int statusCode, String msg) {
        return resultMap(false, statusCode, msg, null);
    }
}
