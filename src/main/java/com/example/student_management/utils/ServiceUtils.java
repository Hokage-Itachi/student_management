package com.example.student_management.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceUtils {
    public static boolean isStringValid(String s, String pattern) {
        if (s == null || s.isBlank()) {
            return false;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(s);
        return matcher.matches();
    }

    public static String sqlExceptionMessageFormat(String s) {
        String[] arr = s.split("Detail: Key ");
        String message = arr[1];
        message = message.replace(")=(", " '");
        message = message.replace("(", "");
        message = message.replace(")", "'");
        return message;
    }

    public static String propertyValueExceptionMessageFormat(String s) {
        String[] arr = s.split("\\.");
        String message = arr[arr.length - 1];
        message = message + " can not be null";
        return message;
    }
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
