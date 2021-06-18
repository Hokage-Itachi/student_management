package com.example.student_management.service;

import com.example.student_management.domain.Permission;
import com.example.student_management.repository.PermistionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermistionService {
    private final PermistionRepository permistionRepository;

    public PermistionService(PermistionRepository permistionRepository) {
        this.permistionRepository = permistionRepository;
    }

    public List<Permission> findAll() {
        return permistionRepository.findAll();
    }

    public Optional<Permission> findById(Long id) {
        return permistionRepository.findById(id);
    }

    public Permission save(Permission permission) {
        return permistionRepository.save(permission);
    }

    public void deleteById(Long id) {
        permistionRepository.deleteById(id);
    }


}
