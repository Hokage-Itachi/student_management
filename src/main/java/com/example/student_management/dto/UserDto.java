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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Date birthday;
    private Date lastLoginDate;
    private Date lockoutDate;
    private Integer loginFailedCount;
    private Date registerDate;
    private String forgotPasswordToken;
    private RoleDto role;
}
