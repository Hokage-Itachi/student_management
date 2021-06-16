package com.example.student_management.converter;

import com.example.student_management.domain.User;
import com.example.student_management.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserDto toDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .birthday(entity.getBirthday())
                .fullName(entity.getFullName())
                .lastLoginDate(entity.getLastLoginDate())
                .lockoutDate(entity.getLockoutDate())
                .loginFailedCount(entity.getLoginFailedCount())
                .registerDate(entity.getRegisterDate())
                .forgotPasswordToken(entity.getForgotPasswordToken())
                .role(entity.getRole().getRoleName())
                .build();
    }

    public User toEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .birthday(userDto.getBirthday())
                .email(userDto.getEmail())
                .fullName(userDto.getFullName())
                .lastLoginDate(userDto.getLastLoginDate())
                .lockoutDate(userDto.getLockoutDate())
                .loginFailedCount(userDto.getLoginFailedCount())
                .registerDate(userDto.getRegisterDate())
                .forgotPasswordToken(userDto.getForgotPasswordToken())
                .build();
    }
}
