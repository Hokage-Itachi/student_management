package com.example.student_management.converter;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Registration;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.EventDto;
import com.example.student_management.service.StudentService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassConverter {

    private final StudentService studentService;
    private final EventConverter eventConverter;

    public ClassConverter(StudentService studentService, EventConverter eventConverter) {
        this.studentService = studentService;
        this.eventConverter = eventConverter;
    }

    public ClassDto toDto(Class entity) {
        List<EventDto> events = new ArrayList<>();
        if (entity.getEvents() != null) {
            events = entity.getEvents().stream().map(eventConverter::toDto).collect(Collectors.toList());
        }
        List<Registration> registrations = entity.getRegistrations();
        List<String> students = new ArrayList<>();
        if (registrations != null) {
            students = registrations.stream().map(registrationId -> studentService.findById(registrationId.getId().getStudentId()).get().getFullName()).collect(Collectors.toList());

        }
        return ClassDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .course(entity.getCourse().getName())
                .teacher(entity.getTeacher().getFullName())
                .events(events)
                .students(students)
                .build();
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
