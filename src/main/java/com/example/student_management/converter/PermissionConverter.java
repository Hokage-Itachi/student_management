package com.example.student_management.converter;

import com.example.student_management.domain.Permission;
import com.example.student_management.dto.PermissionDto;
import org.springframework.stereotype.Component;

@Component
public class PermissionConverter {
    public PermissionDto toDto(Permission entity) {
        return PermissionDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .perName(entity.getPerName())
                .build();
    }

    public Permission toEntity(PermissionDto permissionDto) {
        return Permission.builder()
                .id(permissionDto.getId())
                .description(permissionDto.getDescription())
                .perName(permissionDto.getPerName())
                .build();
    }
}
