package com.example.student_management.dto;

import com.example.student_management.domain.RegistrationId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDto {
    private RegistrationId id;
    private Date registerDay;
    private String status;
    private Date createdDate;
    private ClassDto clazz;
    private StudentDto student;
}
