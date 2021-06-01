package com.example.student_management.dto.converter;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Event;
import com.example.student_management.dto.ClassDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassConverter {
    public ClassDto toDto(Class entity) {
        String teacher = entity.getTeacher().getFullName();
        List<String> events = entity.getEvents().stream().map(Event::getName).collect(Collectors.toList());
        String course = entity.getCourse().getName();
        ClassDto classDto = ClassDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .teacher(teacher)
                .course(course)
                .events(events)
                .build();
        return classDto;
    }

    public Class toEntity(ClassDto classDto) {
        return new Class();
    }
}
