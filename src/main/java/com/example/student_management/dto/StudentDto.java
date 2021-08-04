package com.example.student_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private Long id;
    private String fullName;
    private String address;
    private String email;
    private String phone;
    private Date birthday;
    private String note;
    private String facebook;
    private Date createDate;
}
