package com.example.student_management.converter;

import com.example.student_management.domain.Permistion;
import com.example.student_management.dto.PermistionDto;
import org.springframework.stereotype.Component;

@Component
public class PermistionConverter {
    public PermistionDto toDto(Permistion entity) {
        return PermistionDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .perName(entity.getPerName())
                .build();
    }

    public Permistion toEntity(PermistionDto permistionDto) {
        return Permistion.builder()
                .id(permistionDto.getId())
                .description(permistionDto.getDescription())
                .perName(permistionDto.getPerName())
                .build();
    }
}
