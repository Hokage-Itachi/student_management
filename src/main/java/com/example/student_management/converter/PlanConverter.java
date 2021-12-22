package com.example.student_management.converter;

import com.example.student_management.domain.Plan;
import com.example.student_management.dto.PlanDto;
import org.springframework.stereotype.Component;

@Component
public class PlanConverter {
    private final CourseConverter courseConverter;

    public PlanConverter(CourseConverter courseConverter) {
        this.courseConverter = courseConverter;
    }

    public PlanDto toDto(Plan entity) {
        if(entity == null){
            return null;
        }
        return PlanDto.builder()
                .id(entity.getId())
                .course(courseConverter.toDto(entity.getCourse()))
                .name(entity.getName())
                .build();
    }

    public Plan toEntity(PlanDto planDto) {
        if(planDto == null){
            return null;
        }
        return Plan.builder()
                .id(planDto.getId())
                .name(planDto.getName())
                .course(courseConverter.toEntity(planDto.getCourse()))
                .build();

    }

}
