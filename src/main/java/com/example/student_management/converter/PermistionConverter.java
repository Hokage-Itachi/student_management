package com.example.student_management.converter;

import com.example.student_management.domain.Permission;
import com.example.student_management.dto.PermistionDto;
import org.springframework.stereotype.Component;

@Component
public class PermistionConverter {
    public PermistionDto toDto(Permission entity) {
        return PermistionDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .perName(entity.getPerName())
                .build();
    }

    public Permission toEntity(PermistionDto permistionDto) {
        return Permission.builder()
                .id(permistionDto.getId())
                .description(permistionDto.getDescription())
                .perName(permistionDto.getPerName())
                .build();
    }
}
