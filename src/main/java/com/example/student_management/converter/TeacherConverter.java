package com.example.student_management.converter;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Teacher;
import com.example.student_management.dto.TeacherDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherConverter {
    public TeacherDto toDto(Teacher entity) {
        List<String> classes = new ArrayList<>();
        if (entity.getClasses() != null) {
            classes = entity.getClasses().stream().map(Class::getName).collect(Collectors.toList());
        }
        return TeacherDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .grade(entity.getGrade())
                .classes(classes)
                .build();
    }

    public Teacher toEntity(TeacherDto teacherDto) {
        return Teacher.builder()
                .id(teacherDto.getId())
                .fullName(teacherDto.getFullName())
                .email(teacherDto.getEmail())
                .phone(teacherDto.getPhone())
                .address(teacherDto.getAddress())
                .grade(teacherDto.getGrade())
                .build();
    }
}
