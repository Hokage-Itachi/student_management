package com.example.student_management.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassFilterRequest {
    @Parameter(description = "Search for classes by name containing any matching characters")
    private String[] name;
    @Parameter(description = "Search for classes by start date greater than, in format yyyy-mm-dd")
    private LocalDate startDateFrom;
    @Parameter(description = "Search for classes by start date smaller than, in format yyyy-mm-dd")
    private LocalDate startDateTo;
    @Parameter(description = "Search for classes by end date greater than, in format yyyy-mm-dd")
    private LocalDate endDateFrom;
    @Parameter(description = "Search for classes by end date smaller than, in format yyyy-mm-dd")
    private LocalDate endDateTo;
    @Parameter(description = "Search for classes by status")
    private String[] status;
    @Parameter(description = "Search for classes by course id")
    private Long courseId;
    @Parameter(description = "Search for classes by teacher id")
    private Long teacherId;
}
