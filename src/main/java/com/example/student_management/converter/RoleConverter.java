package com.example.student_management.converter;

import com.example.student_management.domain.Role;
import com.example.student_management.dto.RoleDto;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {
    public RoleDto toDto(Role entity) {
        return RoleDto.builder()
                .roleName(entity.getRoleName())
                .description(entity.getDescriptions())
                .build();
    }

    public Role toEntity(RoleDto roleDto) {
        return Role.builder()
                .roleName(roleDto.getRoleName())
                .descriptions(roleDto.getDescription())
                .build();
    }

}
