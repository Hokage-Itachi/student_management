package com.example.student_management.exception;

import com.example.student_management.utils.ExceptionHandlerUtils;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setStatus(403);
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData("Forbidden", 403, e.getMessage());
        httpServletResponse.getOutputStream().println(new JSONObject(data).toString());
    }
}
