package com.example.student_management.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamDto {
    private Long id;
    private String name;
    private String course;
    private List<String> examResults;
}
