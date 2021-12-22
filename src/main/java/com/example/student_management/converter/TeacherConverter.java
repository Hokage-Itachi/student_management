package com.example.student_management.converter;

import com.example.student_management.domain.Teacher;
import com.example.student_management.dto.TeacherDto;
import org.springframework.stereotype.Component;

@Component
public class TeacherConverter {
    public TeacherDto toDto(Teacher entity) {
        if(entity == null){
            return null;
        }
        return TeacherDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .grade(entity.getGrade())
                .build();
    }

    public Teacher toEntity(TeacherDto teacherDto) {
        if(teacherDto == null){
            return null;
        }
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
