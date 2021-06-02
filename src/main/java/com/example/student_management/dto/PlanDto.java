package com.example.student_management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {
    private Long id;
    private String name;
    private String course;
}
