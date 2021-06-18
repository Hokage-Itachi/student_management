package com.example.student_management.converter;

import com.example.student_management.domain.Role;
import com.example.student_management.domain.User;
import com.example.student_management.dto.RoleDto;
import com.example.student_management.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleConverter {
    private final UserConverter userConverter;

    public RoleConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public RoleDto toDto(Role entity) {
        List<User> userEntities = entity.getUsers();
        List<UserDto> users = new ArrayList<>();
        if (userEntities != null) {
            users = userEntities.stream().map(userConverter::toDto).collect(Collectors.toList());
        }
        return RoleDto.builder()
                .roleName(entity.getRoleName())
                .description(entity.getDescriptions())
                .users(users)
                .build();
    }

    public Role toEntity(RoleDto roleDto) {
        return Role.builder()
                .roleName(roleDto.getRoleName())
                .descriptions(roleDto.getDescription())
                .build();
    }

}
