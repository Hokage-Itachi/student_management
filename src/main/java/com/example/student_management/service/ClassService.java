package com.example.student_management.service;

import com.example.student_management.domain.Class;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.ClassRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClassService {
    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<Class> findAll() {
        return classRepository.findAll();
    }

    public Class findById(Long id) {
        if (id == null) {
            String message = String.format(ExceptionMessage.ID_INVALID.message, "Class");
            log.error("Class ID null");
            throw new DataInvalidException(message);
        }
        Optional<Class> classOptional = classRepository.findById(id);
        if (classOptional.isEmpty()) {
            String message = String.format(ExceptionMessage.CLASS_NOT_FOUND.toString(), id);
            log.error(message);
            throw new ResourceNotFoundException(message);
        }

        return classOptional.get();

    }

    public Class save(Class clazz) {
        if (clazz.getStartDate() == null) {
            log.error("Class start date null");
            throw new DataInvalidException(ExceptionMessage.CLASS_START_DATE_INVALID.message);
        }
        return classRepository.save(clazz);
    }

    public void deleteById(Long id) {
        try {
            classRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error(String.format(ExceptionMessage.CLASS_NOT_FOUND.message, id));
            throw new ResourceNotFoundException(String.format(ExceptionMessage.CLASS_NOT_FOUND.message, id));
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyException(String.format(ExceptionMessage.CLASS_FOREIGN_KEY.message, id));
        }
    }
}
