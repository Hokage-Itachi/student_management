package com.example.student_management.service;

import com.example.student_management.domain.Teacher;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.repository.TeacherRepository;
import com.example.student_management.utils.ServiceUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> findAll(Specification<Teacher> specification, Pageable pageable) {
        if (specification == null) {
            return teacherRepository.findAll(pageable).getContent();
        }
        return teacherRepository.findAll(specification, pageable).getContent();
    }

    @Cacheable(value = "teacher")
    public Teacher findById(Long id) {
        if (id == null) {
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "Teacher"));
        }
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.TEACHER_NOT_FOUND_BY_ID.toString(), id));
        }
        return teacherOptional.get();
    }

    @CachePut(value = "teacher")
    public Teacher save(Teacher teacher) {
        if (!ServiceUtils.isStringValid(teacher.getFullName(), "[^0-9]+")) {
            throw new DataInvalidException(ExceptionMessage.TEACHER_NAME_INVALID.message);
        }
        if (teacher.getEmail() != null && !ServiceUtils.isStringValid(teacher.getEmail(), "^(.+)@(.+)$")) {
            throw new DataInvalidException(ExceptionMessage.TEACHER_EMAIL_INVALID.message);
        }
        try {
            return teacherRepository.save(teacher);
        } catch (DataIntegrityViolationException e) {
            if (e.getRootCause() instanceof SQLException) {
                SQLException ex = (SQLException) e.getRootCause();
                String message = ServiceUtils.sqlExceptionMessageFormat(ex.getMessage());
                throw new ResourceConflictException(message);
            } else {
                throw new DataInvalidException(e.getMessage());
            }

        }
    }

    @CacheEvict(value = "teacher")
    public void deleteById(Long id) {

        try {
            teacherRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.TEACHER_NOT_FOUND_BY_ID.toString(), id));
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyException(String.format(ExceptionMessage.TEACHER_FOREIGN_KEY.toString(), id));
        }
    }
}
