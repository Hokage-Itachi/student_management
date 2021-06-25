package com.example.student_management.utils;

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
}
