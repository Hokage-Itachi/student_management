package com.example.student_management.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String grade;
    private List<String> classes;
}
