package com.example.student_management.service;

import com.example.student_management.domain.Student;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.StudentRepository;
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

    public Student save(Student student) {
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
