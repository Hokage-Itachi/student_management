package com.example.student_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {
    private Long id;
    private String name;
    private CourseDto course;
}
