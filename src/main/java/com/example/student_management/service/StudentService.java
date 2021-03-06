package com.example.student_management.service;

import com.example.student_management.domain.Student;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.repository.StudentRepository;
import com.example.student_management.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll(Specification<Student> specification, Pageable pageable) {
        if (specification == null) {
            return studentRepository.findAll(pageable).getContent();
        }
        return studentRepository.findAll(specification, pageable).getContent();
    }

    @Cacheable(value = "student")
    public Student findById(Long id) {
        if (id == null) {
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "Student"));
        }
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.STUDENT_NOT_FOUND.toString(), id));
        }
        return studentOptional.get();
    }

    @CachePut(value = "student")
    public Student save(Student student) {
        if (!ServiceUtils.isStringValid(student.getFullName(), "[^0-9]+")) {
            throw new DataInvalidException(ExceptionMessage.STUDENT_NAME_INVALID.message);
        }
        if (!ServiceUtils.isStringValid(student.getEmail(), "^(.+)@(.+)$")) {
            throw new DataInvalidException(ExceptionMessage.STUDENT_EMAIL_INVALID.message);
        }
        if (!ServiceUtils.isStringValid(student.getAddress(), "[^$&+,:;=?@#|'<>.^*()%!-]+")) {
            throw new DataInvalidException(ExceptionMessage.STUDENT_ADDRESS_INVALID.message);
        }
        try {
            return studentRepository.save(student);

        } catch (DataIntegrityViolationException e) {
            if (e.getRootCause() instanceof SQLException) {
                SQLException ex = (SQLException) e.getRootCause();
                String message = ServiceUtils.sqlExceptionMessageFormat(ex.getMessage());
                throw new ResourceConflictException(message);
            }
            throw new DataInvalidException(e.getMessage());

        }
    }

    @CacheEvict(value = "student")
    public void deleteById(Long id) {
        try {
            studentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.STUDENT_NOT_FOUND.toString(), id));
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyException(String.format(ExceptionMessage.STUDENT_FOREIGN_KEY.toString(), id));
        }
    }

}
