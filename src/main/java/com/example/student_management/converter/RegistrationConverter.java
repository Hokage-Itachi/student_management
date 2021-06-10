package com.example.student_management.converter;

import com.example.student_management.domain.Registration;
import com.example.student_management.dto.RegistrationDto;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConverter {
    public RegistrationDto toDto(Registration entity) {
        return RegistrationDto.builder()
                .id(entity.getId())
                .registerDay(entity.getRegisterDay())
                .status(entity.getStatus())
                .createdDate(entity.getCreateDate())
                .clazz(entity.getClazz().getName())
                .student(entity.getStudent().getFullName())
                .build();
    }

    public Registration toEntity(RegistrationDto registrationDto) {
        return Registration.builder()
                .id(registrationDto.getId())
                .registerDay(registrationDto.getRegisterDay())
                .status(registrationDto.getStatus())
                .createDate(registrationDto.getCreatedDate())
                .build();
    }
}
