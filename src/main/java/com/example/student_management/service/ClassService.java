package com.example.student_management.service;

import com.example.student_management.domain.Class;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.repository.ClassRepository;
import com.example.student_management.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClassService {
    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<Class> findAll(Pageable pageable, Specification<Class> specification) {
        if (specification != null) {
            return classRepository.findAll(specification, pageable).getContent();
        }
        return classRepository.findAll(pageable).getContent();
    }

    @Cacheable(value = "class")
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

    @CachePut(value = "class")
    public Class save(Class clazz) {
        if (clazz.getStartDate() == null) {
            log.error("Class start date null");
            throw new DataInvalidException(ExceptionMessage.CLASS_START_DATE_INVALID.message);
        }
        if (clazz.getTeacher() == null || clazz.getTeacher().getId() == null) {
            log.error("Teacher reference null");
            throw new ForeignKeyException(String.format(ExceptionMessage.NULL_FOREIGN_KEY_REFERENCE.message, "Teacher"));
        }
        if (clazz.getCourse() == null || clazz.getCourse().getId() == null) {
            log.error("Course reference null");
            throw new ForeignKeyException(String.format(ExceptionMessage.NULL_FOREIGN_KEY_REFERENCE.message, "Course"));
        }
        try {
            return classRepository.save(clazz);
        } catch (DataIntegrityViolationException e) {
            SQLException ex = (SQLException) e.getRootCause();
            throw new ResourceNotFoundException(ServiceUtils.sqlExceptionMessageFormat(ex.getMessage()));
        }
    }

    @CacheEvict(value = "class")
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
