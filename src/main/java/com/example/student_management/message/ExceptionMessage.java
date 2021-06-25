package com.example.student_management.message;

public enum ExceptionMessage {
    // Not found exception message
    CLASS_NOT_FOUND("Class with id %d not found"),
    COURSE_NOT_FOUND("Course with id %d not found"),
    EVENT_NOT_FOUND("Event with id %d not found"),
    EXAM_NOT_FOUND("Exam with id %d not found"),
    EXAM_RESULT_NOT_FOUND("Exam result with id %d not found"),
    PERMISSION_NOT_FOUND("Permission with id %d not found"),
    PLAN_NOT_FOUND("Plan with id %d not found"),
    REGISTRATION_NOT_FOUND("Registration with id %d not found"),
    ROLE_NOT_FOUND("Role with name '%s' not found"),
    STUDENT_NOT_FOUND("Student with id %d not found"),
    TEACHER_NOT_FOUND_BY_ID("Teacher with id %d not found"),
    TEACHER_NOT_FOUND_BY_EMAIL("Teacher with email '%s' not found"),
    USER_NOT_FOUND_BY_ID("User with id %d not found"),
    USER_NOT_FOUND_BY_EMAIL("User with email '%s' not found"),
    USER_NOT_FOUND_BY_USERNAME("User with username '%s' not found"),

    // Invalid expression message
    ROLE_NAME_INVALID("Role name invalid");
    public final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
