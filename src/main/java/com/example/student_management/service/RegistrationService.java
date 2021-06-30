package com.example.student_management.service;

import com.example.student_management.domain.Registration;
import com.example.student_management.domain.RegistrationId;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.RegistrationRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;

    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public List<Registration> findAll() {
        return registrationRepository.findAll();
    }

    public Registration findById(RegistrationId id) {
        Optional<Registration> registrationOptional = registrationRepository.findById(id);
        if (registrationOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.REGISTRATION_NOT_FOUND.toString(), id.getStudentId(), id.getClassId()));
        }
        return registrationOptional.get();
    }

    public Registration save(Registration registration) {
        return registrationRepository.save(registration);
    }

    public void deleteById(RegistrationId id) {
        try {
            registrationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.REGISTRATION_NOT_FOUND.toString(), id.getStudentId(), id.getClassId()));
        }
    }
}