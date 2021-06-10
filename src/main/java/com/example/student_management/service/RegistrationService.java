package com.example.student_management.service;

import com.example.student_management.domain.Registration;
import com.example.student_management.domain.RegistrationId;
import com.example.student_management.repository.RegistrationRepository;
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

    public Optional<Registration> findById(RegistrationId id) {
        return registrationRepository.findById(id);
    }

    public Registration save(Registration registration) {
        return registrationRepository.save(registration);
    }

    public void deleteById(RegistrationId id) {
        registrationRepository.deleteById(id);
    }
}