package com.example.student_management.converter;

import com.example.student_management.domain.User;
import com.example.student_management.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private final RoleConverter roleConverter;

    public UserConverter(RoleConverter roleConverter) {
        this.roleConverter = roleConverter;
    }

    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
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
                .role(roleConverter.toDto(entity.getRole()))
                .build();
    }

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
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
                .role(roleConverter.toEntity(userDto.getRole()))
                .build();
    }
}
