package com.example.student_management.converter;

import com.example.student_management.domain.Registration;
import com.example.student_management.domain.Student;
import com.example.student_management.dto.StudentDto;
import com.example.student_management.service.ClassService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentConverter {
    private final ClassService classService;

    public StudentConverter(ClassService classService) {
        this.classService = classService;
    }

    public StudentDto toDto(Student entity) {
        List<Registration> registrations = entity.getRegistrations();
        List<String> classes = new ArrayList<>();
        if (registrations != null) {
            classes = registrations.stream().map(registrationId -> classService.findById(registrationId.getId().getClassId()).getName()).collect(Collectors.toList());
        }
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

    public Student toEntity(StudentDto studentDto) {
        return Student.builder()
                .id(studentDto.getId())
                .fullName(studentDto.getFullName())
                .address(studentDto.getAddress())
                .email(studentDto.getEmail())
                .phone(studentDto.getPhone())
                .birthday(studentDto.getBirthday())
                .note(studentDto.getNote())
                .facebook(studentDto.getFacebook())
                .createDate(studentDto.getCreateDate())
                .build();
    }
}
