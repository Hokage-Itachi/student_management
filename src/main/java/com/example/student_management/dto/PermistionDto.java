package com.example.student_management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermistionDto {
    private Long id;
    private String description;
    private String perName;
}
