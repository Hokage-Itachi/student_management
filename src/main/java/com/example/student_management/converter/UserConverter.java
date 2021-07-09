package com.example.student_management.converter;

import com.example.student_management.domain.Permission;
import com.example.student_management.domain.User;
import com.example.student_management.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    public UserDto toDto(User entity) {
        Set<Permission> permissions = entity.getRole().getPermissions();
        if (entity.getPermissions() != null) {
            permissions.addAll(entity.getPermissions());
        }
        List<String> permissionDtoList = permissions.stream().map(Permission::getPerName).collect(Collectors.toList());
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password("")
                .email(entity.getEmail())
                .birthday(entity.getBirthday())
                .fullName(entity.getFullName())
                .lastLoginDate(entity.getLastLoginDate())
                .lockoutDate(entity.getLockoutDate())
                .loginFailedCount(entity.getLoginFailedCount())
                .registerDate(entity.getRegisterDate())
                .forgotPasswordToken(entity.getForgotPasswordToken())
                .role(entity.getRole().getRoleName())
                .permissions(permissionDtoList)
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
