package com.example.student_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String grade;
}
