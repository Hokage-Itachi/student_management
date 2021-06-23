package com.example.student_management.service;

import com.example.student_management.domain.Class;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.ClassRepository;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public Class findById(Long id) {
        Optional<Class> classOptional = classRepository.findById(id);
        if (classOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.CLASS_NOT_FOUND.toString(), id));
        }

        return classOptional.get();

    }

    public Class save(Class clazz) {
        return classRepository.save(clazz);
    }

    public void deleteById(Long id) {
        try {
            classRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format("Class with id %d not found", id));
        }
    }
}
