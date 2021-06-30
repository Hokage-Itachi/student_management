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
        String error = "Username or password invalid";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        String error = "Resource not found";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, 404, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataInvalidException.class)
    public ResponseEntity<Object> handleDataInvalidException(DataInvalidException e, WebRequest request) {
        String error = "Data invalid";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Object> handleResourceConflictException(ResourceConflictException e, WebRequest request) {
        String error = "Resource conflict";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, 409, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForeignKeyException.class)
    public ResponseEntity<Object> handleForeignKeyException(ForeignKeyException e, WebRequest request){
        String error = "Foreign key error";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, 400, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

}
