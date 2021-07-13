package com.example.student_management.converter;

import com.example.student_management.domain.Student;
import com.example.student_management.dto.StudentDto;
import org.springframework.stereotype.Component;

@Component
public class StudentConverter {

    public StudentDto toDto(Student entity) {
        if(entity == null){
            return null;
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
                .build();
    }

    public Student toEntity(StudentDto studentDto) {
        if(studentDto == null){
            return null;
        }
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
