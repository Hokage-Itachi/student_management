package com.example.student_management.converter;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Student;
import com.example.student_management.dto.StudentDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentConverter {
    public StudentDto toDto(Student entity) {
        List<String> classes = entity.getClasses().stream().map(Class::getName).collect(Collectors.toList());
        return StudentDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .address(entity.getAddress())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .birthday(entity.getBirthday())
                .note(entity.getNote())
                .facebook(entity.getFacebook())
                .createDate(entity.getCreateDate())
                .classes(classes)
                .build();
    }
}
