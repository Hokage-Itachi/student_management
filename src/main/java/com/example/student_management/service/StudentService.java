package com.example.student_management.service;

import com.example.student_management.domain.Student;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.StudentRepository;
import com.example.student_management.utils.ServiceUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.STUDENT_NOT_FOUND.toString(), id));
        }
        return studentOptional.get();
    }

    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

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
            throw new ResourceConflictException(String.format(ExceptionMessage.STUDENT_EMAIL_CONFLICT.toString(), student.getEmail()));
        }
    }

    public void deleteById(Long id) {
        try {
            studentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.STUDENT_NOT_FOUND.toString(), id));
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyException(String.format(ExceptionMessage.STUDENT_FOREIGN_KEY_EXCEPTION_MESSAGE.toString(), id));
        }
    }

}
