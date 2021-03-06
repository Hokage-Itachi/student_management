package com.example.student_management.exception;

import com.example.student_management.utils.ExceptionHandlerUtils;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData("unauthorized", e.getMessage());
        httpServletResponse.getOutputStream().println((new JSONObject(data)).toString());

    }
}