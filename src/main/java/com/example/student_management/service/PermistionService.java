package com.example.student_management.service;

import com.example.student_management.domain.Permistion;
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

    public List<Permistion> findAll() {
        return permistionRepository.findAll();
    }

    public Optional<Permistion> findById(Long id) {
        return permistionRepository.findById(id);
    }

    public Permistion save(Permistion permistion) {
        return permistionRepository.save(permistion);
    }

    public void deleteById(Long id) {
        permistionRepository.deleteById(id);
    }


}
