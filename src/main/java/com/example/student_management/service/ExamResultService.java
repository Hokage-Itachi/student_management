package com.example.student_management.service;

import com.example.student_management.domain.ExamResult;
import com.example.student_management.repository.ExamResultRepository;
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

    public Optional<ExamResult> findById(Long id) {
        return examResultRepository.findById(id);
    }

    public ExamResult save(ExamResult examResult) {
        return examResultRepository.save(examResult);
    }

    public void deleteById(Long id) {
        examResultRepository.deleteById(id);
    }
}
