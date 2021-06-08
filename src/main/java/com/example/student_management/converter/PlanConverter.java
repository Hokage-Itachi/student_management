package com.example.student_management.converter;

import com.example.student_management.domain.Plan;
import com.example.student_management.dto.PlanDto;
import org.springframework.stereotype.Component;

@Component
public class PlanConverter {
    public PlanDto toDto(Plan entity) {
        return PlanDto.builder()
                .id(entity.getId())
                .course(entity.getCourse().getName())
                .name(entity.getName())
                .build();
    }

    public Plan toEntity(PlanDto planDto) {
        return Plan.builder()
                .id(planDto.getId())
                .name(planDto.getName())
                .build();

    }

}
