package com.example.student_management.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Date birthday;
    private Date lastLoginDate;
    private Date lockoutDate;
    private Integer loginFailedCount;
    private Date registerDate;
    private String forgotPasswordToken;
    private String role;
}
