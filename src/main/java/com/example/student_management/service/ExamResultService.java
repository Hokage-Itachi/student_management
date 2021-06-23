package com.example.student_management.service;

import com.example.student_management.domain.ExamResult;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.ExamResultRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamResultService {
    private final ExamResultRepository examResultRepository;

    public ExamResultService(ExamResultRepository examResultRepository) {
        this.examResultRepository = examResultRepository;
    }

    public List<ExamResult> findAll() {
        return examResultRepository.findAll();
    }

    public ExamResult findById(Long id) {
        Optional<ExamResult> examResultOptional = examResultRepository.findById(id);
        if (examResultOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EXAM_RESULT_NOT_FOUND.toString(), id));
        }
        return examResultOptional.get();
    }

    public ExamResult save(ExamResult examResult) {
        return examResultRepository.save(examResult);
    }

    public void deleteById(Long id) {
        try {
            examResultRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EXAM_RESULT_NOT_FOUND.toString(), id));
        }
    }
}
