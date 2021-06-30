package com.example.student_management.utils;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExceptionHandlerUtils {
    public static Map<String, Object> createResponseData(String error, Integer status, String message) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("timestamp", new Timestamp(System.currentTimeMillis()));
        data.put("status", status);
        data.put("error", error);
        data.put("message", message);
        return data;
    }
}
