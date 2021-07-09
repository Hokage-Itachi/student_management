package com.example.student_management.message;

public enum ExceptionMessage {
    // Not found exception message
    CLASS_NOT_FOUND("Class %d not found"),
    COURSE_NOT_FOUND("Course %d not found"),
    EVENT_NOT_FOUND("Event %d not found"),
    EXAM_NOT_FOUND("Exam %d not found"),
    EXAM_RESULT_NOT_FOUND("Exam result %d not found"),
    PERMISSION_NOT_FOUND("Permission %d not found"),
    PLAN_NOT_FOUND("Plan %d not found"),
    REGISTRATION_NOT_FOUND("No registration of student %d for class %d"),
    ROLE_NOT_FOUND("Role '%s' not found"),
    STUDENT_NOT_FOUND("Student %d not found"),
    TEACHER_NOT_FOUND_BY_ID("Teacher %d not found"),
    TEACHER_NOT_FOUND_BY_EMAIL("Teacher email '%s' not found"),
    USER_NOT_FOUND_BY_ID("User %d not found"),
    USER_NOT_FOUND_BY_EMAIL("User email '%s' not found"),
    USER_NOT_FOUND_BY_USERNAME("User '%s' not found"),
    USER_PERMISSION_NOT_FOUND("User %d does not have permission %d"),
    ROLE_PERMISSION_NOT_FOUND("Role '%s' does not have permission %d"),

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
    CREATE_DATE_INVALID("Create date invalid"),
    EVENT_STATUS_INVALID("Event status invalid"),
    COURSE_NAME_INVALID("Course name invalid"),
    CLASS_START_DATE_INVALID("Start date invalid"),
    TOKEN_INVALID("Token invalid"),
    ID_INVALID("%s ID must not be null"),


    // Resource conflict message
    ROLE_NAME_CONFLICT("Role '%s' already exists"),
    REGISTRATION_CONFLICT("Registration of student %d for class %d already exists"),
    USER_AUTHORIZATION_CONFLICT("User %d already has permission %d"),
    ROLE_AUTHORIZATION_CONFLICT("Role '%s' already has permission %d"),
    EXAM_RESULT_CONFLICT("Exam result already exists"),


    // Foreign key exception message
    TEACHER_FOREIGN_KEY("Teacher %d still has foreign key reference"),
    STUDENT_FOREIGN_KEY("Student %d still has foreign key reference"),
    PERMISSION_FOREIGN_KEY("Permission '%d' still has foreign key reference"),
    EXAM_FOREIGN_KEY("Exam '%d' still has foreign key reference"),
    COURSE_FOREIGN_KEY("Course %d still has foreign key reference"),
    CLASS_FOREIGN_KEY("Class %d still has foreign key reference"),
    ROLE_FOREIGN_KEY("Role '%s' still has foreign key reference")
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
