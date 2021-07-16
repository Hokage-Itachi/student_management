package com.example.student_management.utils;

import com.example.student_management.specification.SearchCriteria;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ServiceUtils {
    private static final String[] ALLOWED_OPERATORS = {
            "eq", "ne", "gt", "lt", "li", "st", "ew", "nl", "ct", "nc"};

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

    public static List<SearchCriteria> getFilterParam(String[] filter, Class targetClass) {
        if (filter == null || filter.length == 0) {
            return null;
        }
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        for (String s : filter) {
            String[] arr = validateFilter(s, targetClass);

            searchCriteria.add(new SearchCriteria(arr[0], arr[1], arr[2]));
        }
        return searchCriteria;
    }

    public static Sort getSortParam(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String s : sort) {
            String[] arr = s.split(":");
            orders.add(new Sort.Order(getSortDirection(arr[1]), arr[0]));
        }
        return Sort.by(orders);
    }

    private static Direction getSortDirection(String direction) {
        String dir = direction.toLowerCase();
        if (dir.equals("asc")) {
            return Direction.ASC;
        }
        if (dir.equals("desc")) {
            return Direction.DESC;
        }

        throw new IllegalArgumentException(String.format("Illegal sort direction '%s'", dir));
    }

    private static String[] validateFilter(String filter, Class targetClass) {
        String format = "key:operation:value";
        String[] arr = filter.split(":");
        if (arr.length != 3) {
            throw new IllegalArgumentException(String.format("Filter option '%s' malformed '%s'", filter, format));
        }
        List<String> fields = Arrays.stream(targetClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        if (!fields.contains(arr[0])) {
            throw new IllegalArgumentException(String.format("%s have no attribute '%s'", targetClass.getSimpleName(), arr[0]));
        }
        if (!getAllowedOperator().contains(arr[1])) {
            throw new IllegalArgumentException(String.format("Operator '%s' not allowed", arr[1]));
        }
        return arr;
    }

    private static String[] validateSortParam(String sort, Class targetClass) {
        String sortFormat = "key:direction";
        String[] arr = sort.split(":");
        if (arr.length != 2) {
            throw new IllegalArgumentException(String.format("Sort option '%s' malformed '%s'", sort, sortFormat));
        }
        List<String> fields = Arrays.stream(targetClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        if (!fields.contains(arr[0])) {
            throw new IllegalArgumentException(String.format("%s have no attribute '%s'", targetClass.getSimpleName(), arr[0]));
        }
        return arr;
    }

    private static List<String> getAllowedOperator() {
        return Arrays.asList(ALLOWED_OPERATORS);
    }
}
