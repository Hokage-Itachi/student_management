package com.example.student_management.enums;

public enum SearchOperation {
    EQUALITY,
    NEGATION,
    LESS_THAN,
    GREATER_THAN,
    LIKE,
    NOT_LIKE,
    STARTS_WITH,
    ENDS_WITH,
    CONTAINS,
    NOT_CONTAINS;

    public static SearchOperation getSearchOperation(String operation) {
        switch (operation) {
            case "eq":
                return EQUALITY;
            case "ne":
                return NEGATION;
            case "gt":
                return GREATER_THAN;
            case "lt":
                return LESS_THAN;
            case "li":
                return LIKE;
            case "st":
                return STARTS_WITH;
            case "ew":
                return ENDS_WITH;
            case "nl":
                return NOT_LIKE;
            case "ct":
                return CONTAINS;
            case "nc":
                return NOT_CONTAINS;
            default:
                return null;
        }
    }
}
