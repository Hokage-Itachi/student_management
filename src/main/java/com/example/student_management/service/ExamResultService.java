package com.example.student_management.service;

import com.example.student_management.domain.ExamResult;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ResourceConflictException;
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
        if (id == null) {
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "Exam result"));
        }
        Optional<ExamResult> examResultOptional = examResultRepository.findById(id);
        if (examResultOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EXAM_RESULT_NOT_FOUND.toString(), id));
        }
        return examResultOptional.get();
    }

    public ExamResult update(ExamResult examResult) {
        if (examResult.getScore() == null) {
            throw new DataInvalidException(ExceptionMessage.EXAM_RESULT_SCORE_INVALID.message);
        }
        if (examResult.getResultDate() == null) {
            throw new DataInvalidException(ExceptionMessage.EXAM_RESULT_DATE_INVALID.message);
        }
        Optional<ExamResult> examResultOptional = examResultRepository.findByStudentIdAndExamIdAndClazzId(examResult.getStudent().getId(), examResult.getExam().getId(), examResult.getClazz().getId());
        if (examResultOptional.isPresent() && examResultOptional.get().getId().equals(examResult.getId())) {
            throw new ResourceConflictException(ExceptionMessage.EXAM_RESULT_CONFLICT.message);
        }
        return examResultRepository.save(examResult);
    }

    public ExamResult add(ExamResult examResult) {
        if (examResult.getScore() == null) {
            throw new DataInvalidException(ExceptionMessage.EXAM_RESULT_SCORE_INVALID.message);
        }
        if (examResult.getResultDate() == null) {
            throw new DataInvalidException(ExceptionMessage.EXAM_RESULT_DATE_INVALID.message);
        }
        Optional<ExamResult> examResultOptional = examResultRepository.findByStudentIdAndExamIdAndClazzId(examResult.getStudent().getId(), examResult.getExam().getId(), examResult.getClazz().getId());
        if (examResultOptional.isPresent()) {
            throw new ResourceConflictException(ExceptionMessage.EXAM_RESULT_CONFLICT.message);
        }
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
