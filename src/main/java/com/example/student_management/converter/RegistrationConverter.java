package com.example.student_management.converter;

import com.example.student_management.domain.Registration;
import com.example.student_management.dto.RegistrationDto;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConverter {
    private final ClassConverter classConverter;
    private final StudentConverter studentConverter;

    public RegistrationConverter(ClassConverter classConverter, StudentConverter studentConverter) {
        this.classConverter = classConverter;
        this.studentConverter = studentConverter;
    }

    public RegistrationDto toDto(Registration entity) {
        if(entity == null){
            return null;
        }
        return RegistrationDto.builder()
                .id(entity.getId())
                .registerDay(entity.getRegisterDay())
                .status(entity.getStatus())
                .createdDate(entity.getCreateDate())
                .clazz(classConverter.toDto(entity.getClazz()))
                .student(studentConverter.toDto(entity.getStudent()))
                .build();
    }

    public Registration toEntity(RegistrationDto registrationDto) {
        if(registrationDto == null){
            return null;
        }
        return Registration.builder()
                .id(registrationDto.getId())
                .registerDay(registrationDto.getRegisterDay())
                .status(registrationDto.getStatus())
                .createDate(registrationDto.getCreatedDate())
                .clazz(classConverter.toEntity(registrationDto.getClazz()))
                .student(studentConverter.toEntity(registrationDto.getStudent()))
                .build();
    }
}
