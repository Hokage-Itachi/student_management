package com.example.student_management.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassDto {
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String status;
    private String course;
    private TeacherDto teacher;
    private List<String> events;
    private List<String> students;
}
