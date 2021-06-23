package com.example.student_management.service;

import com.example.student_management.domain.Exam;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.ExamRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamService {
    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    public Exam findById(Long id) {

        Optional<Exam> examOptional = examRepository.findById(id);
        if (examOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EXAM_NOT_FOUND.toString(), id));
        }
        return examOptional.get();
    }

    public Exam save(Exam exam) {
        return examRepository.save(exam);
    }

    public void deleteById(Long id) {
        try {
            examRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EXAM_NOT_FOUND.toString(), id));
        }
    }
}
