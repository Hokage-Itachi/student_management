package com.example.student_management.exception;

import com.example.student_management.utils.ExceptionHandlerUtils;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleException(Exception e) {
//        String error = "Internal Server Error";
//        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, e.getMessage());
//        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadRequestException(BadCredentialsException e, WebRequest request) {
        String message = "Username or password invalid";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(e.getMessage(), message);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        String error = "Resource not found";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataInvalidException.class)
    public ResponseEntity<Object> handleDataInvalidException(DataInvalidException e, WebRequest request) {
        String error = "Data invalid";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Object> handleResourceConflictException(ResourceConflictException e, WebRequest request) {
        String error = "Resource conflict";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForeignKeyException.class)
    public ResponseEntity<Object> handleForeignKeyException(ForeignKeyException e, WebRequest request) {
        String error = "Foreign key error";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Object> handleMessagingException(MessagingException e, WebRequest request) {
        String error = "Error while sending mail";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, "");
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<Object> handleUnsupportedEncodingException(UnsupportedEncodingException e, WebRequest request) {
        String error = "Error while sending mail";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, "");
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String error = "Method not allowed";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String error = "Argument type mismatch";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String error = "Json error";
        String message = e.getMessage().split(";")[0];
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, message);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        String error = "Illegal argument";
        String message = e.getMessage();
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, message);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException e) {
        String error = "Illegal argument";
        String message = "Data of field '" + e.getBindingResult().getFieldError().getField() + "' mismatch";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, message);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException e) {
        String error = "Illegal argument";
        String message = "Property '" + e.getPropertyName() + "' not found";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, message);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String error = "Missing require parameter";
        String message = "Missing required parameter '" + e.getParameterName() + "' ";
        Map<String, Object> data = ExceptionHandlerUtils.createResponseData(error, message);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }


}
