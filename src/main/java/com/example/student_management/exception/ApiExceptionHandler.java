package com.example.student_management.exception;

import com.example.student_management.utils.ExceptionHandlerUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadRequestException(BadCredentialsException e, WebRequest request) {
        String message = "Username or password invalid";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException e, WebRequest request) {
        String message = "Invalid JWT token";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e, WebRequest request) {
        String message = "JWT token expired";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<Object> handleUnsupportedJwtException(UnsupportedJwtException e, WebRequest request) {
        String message = "Unsupported JWT token";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        String message = "JWT claims is empty";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e, WebRequest request){
        String message = "Forbidden";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 403, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
    }
}
