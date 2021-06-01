package com.example.student_management.service;

import com.example.student_management.domain.Class;
import com.example.student_management.repository.ClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {
    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<Class> findAll() {
        return classRepository.findAll();
    }

    public Optional<Class> findById(Long id) {
        return classRepository.findById(id);
    }

    public Class save(Class clazz) {
        return classRepository.save(clazz);
    }

    public void deleteById(Long id) {
        classRepository.deleteById(id);
    }
}
