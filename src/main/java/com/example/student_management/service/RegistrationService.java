package com.example.student_management.service;

import com.example.student_management.domain.Registration;
import com.example.student_management.domain.RegistrationId;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.repository.RegistrationRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;

    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public List<Registration> findAll(Specification<Registration> specification, Pageable pageable) {
        if (specification == null) {
            return registrationRepository.findAll(pageable).getContent();
        }
        return registrationRepository.findAll(specification, pageable).getContent();
    }

    @Cacheable(value = "registration")
    public Registration findById(RegistrationId id) {
        Optional<Registration> registrationOptional = registrationRepository.findById(id);
        if (registrationOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.REGISTRATION_NOT_FOUND.toString(), id.getStudentId(), id.getClassId()));
        }
        return registrationOptional.get();
    }

    @CachePut(value = "registration")
    public Registration update(Registration registration) {
        if (registrationRepository.findById(registration.getId()).isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.REGISTRATION_NOT_FOUND.toString(), registration.getId().getStudentId(), registration.getId().getClassId()));
        }
        return registrationRepository.save(registration);
    }

    @CachePut(value = "registration")
    public Registration add(Registration registration) {
        if (registrationRepository.findById(registration.getId()).isPresent()) {
            throw new ResourceConflictException(String.format(ExceptionMessage.REGISTRATION_CONFLICT.toString(), registration.getId().getStudentId(), registration.getId().getClassId()));
        }
        return registrationRepository.save(registration);
    }

    @CacheEvict(value = "registration")
    public void deleteById(RegistrationId id) {
        try {
            registrationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.REGISTRATION_NOT_FOUND.toString(), id.getStudentId(), id.getClassId()));
        }
    }
}