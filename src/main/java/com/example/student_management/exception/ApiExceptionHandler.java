package com.example.student_management.exception;

import com.example.student_management.utils.ExceptionHandlerUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadRequestException(BadCredentialsException e, WebRequest request) {
        String message = "Username or password invalid";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        String message = "Resource not found";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 404, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataInvalidException.class)
    public ResponseEntity<Object> handleDataInvalidException(DataInvalidException e, WebRequest request) {
        String message = "Data invalid";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Object> handleResourceConflictException(ResourceConflictException e, WebRequest request) {
        String message = "Resource conflict";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(message, 409, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.CONFLICT);
    }

}
