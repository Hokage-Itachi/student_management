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
    REGISTRATION_NOT_FOUND("No registration of student %d for class %d"),
    ROLE_NOT_FOUND("Role with name '%s' not found"),
    STUDENT_NOT_FOUND("Student with id %d not found"),
    TEACHER_NOT_FOUND_BY_ID("Teacher with id %d not found"),
    TEACHER_NOT_FOUND_BY_EMAIL("Teacher with email '%s' not found"),
    USER_NOT_FOUND_BY_ID("User with id %d not found"),
    USER_NOT_FOUND_BY_EMAIL("User with email '%s' not found"),
    USER_NOT_FOUND_BY_USERNAME("User with username '%s' not found"),

    // Invalid expression message
    STUDENT_NAME_INVALID("Student name invalid"),
    STUDENT_EMAIL_INVALID("Student email invalid"),
    STUDENT_ADDRESS_INVALID("Student address invalid"),
    TEACHER_NAME_INVALID("Teacher name invalid"),
    TEACHER_EMAIL_INVALID("Teacher email invalid"),
    ROLE_NAME_INVALID("Role name invalid"),
    PERMISSION_NAME_INVALID("Permission name invalid"),
    EXAM_RESULT_SCORE_INVALID("Exam result score invalid"),
    EXAM_RESULT_DATE_INVALID("Register date invalid"),
    EXAM_NAME_INVALID("Exam name invalid"),
    EVENT_NAME_INVALID("Event name invalid"),
    EVENT_CREATE_DATE_INVALID("Create date invalid"),
    EVENT_STATUS_INVALID("Event status invalid"),


    // Resource conflict message
    ROLE_NAME_CONFLICT("Role '%s' already exists"),
    REGISTRATION_CONFLICT("Registration of student '%d' for class '%d' already exists"),


    // Foreign key exception message
    TEACHER_FOREIGN_KEY("Teacher %d still has foreign key reference"),
    STUDENT_FOREIGN_KEY("Student %d still has foreign key reference"),
    PERMISSION_FOREIGN_KEY("Permission '%d' still has foreign key reference"),
    EXAM_FOREIGN_KEY("Exam '%d' still has foreign key reference"),
    COURSE_FOREIGN_KEY("Course '%d' still has foreign key reference")
    ;
    public final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
