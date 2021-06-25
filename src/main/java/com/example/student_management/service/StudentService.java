package com.example.student_management.service;

import com.example.student_management.domain.Student;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.StudentRepository;
import com.example.student_management.utils.ServiceUtils;
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
        Optional<Student> studentOptional = findByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            if (!studentOptional.get().getEmail().equals(student.getEmail())) {
                // TODO: consider throw conflict exception
                throw new DataInvalidException(String.format(ExceptionMessage.STUDENT_EMAIL_DUPLICATE.toString(), student.getEmail()));
            }
        }
        return studentRepository.save(student);
    }

    public void deleteById(Long id) {
        try {
            studentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.STUDENT_NOT_FOUND.toString(), id));
        }
    }

}
