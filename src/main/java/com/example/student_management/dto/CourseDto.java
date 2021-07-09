package com.example.student_management.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
    private Long id;
    private String name;
    private String type;
    private Date createdDate;
    private List<ClassDto> classes;
    private List<ExamDto> exams;
    private List<PlanDto> plans;

}
