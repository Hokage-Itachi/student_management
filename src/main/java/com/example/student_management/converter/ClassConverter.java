package com.example.student_management.converter;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Event;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.CourseDto;
import com.example.student_management.dto.EventDto;
import com.example.student_management.dto.TeacherDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassConverter {

    private TeacherConverter teacherConverter;
    private EventConverter eventConverter;

    public ClassConverter(TeacherConverter teacherConverter, EventConverter eventConverter) {
        this.teacherConverter = teacherConverter;
        this.eventConverter = eventConverter;
    }

    public ClassDto toDto(Class entity) {
        List<EventDto> events = entity.getEvents().stream().map(eventConverter::toDto).collect(Collectors.toList());
        ClassDto classDto = ClassDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .course(entity.getCourse().getName())
                .teacher(entity.getTeacher().getFullName())
                .events(events)
                .build();
        return classDto;
    }

    public Class toEntity(ClassDto classDto) {

        return Class.builder()
                .id(classDto.getId())
                .name(classDto.getName())
                .startDate(classDto.getStartDate())
                .endDate(classDto.getEndDate())
                .status(classDto.getStatus())
                .build();
    }
}
