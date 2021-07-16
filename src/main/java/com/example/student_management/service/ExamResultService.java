package com.example.student_management.service;

import com.example.student_management.domain.ExamResult;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.repository.ExamResultRepository;
import com.example.student_management.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
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
public class ExamResultService {
    private final ExamResultRepository examResultRepository;

    public ExamResultService(ExamResultRepository examResultRepository) {
        this.examResultRepository = examResultRepository;
    }

    public List<ExamResult> findAll(Specification<ExamResult> specification, Pageable pageable) {
        if (specification == null) {
            return examResultRepository.findAll(pageable).getContent();
        }
        return examResultRepository.findAll(specification, pageable).getContent();
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

    public ExamResult save(ExamResult examResult) {
        if (examResult.getScore() == null) {
            log.error("Exam result score null");
            throw new DataInvalidException(ExceptionMessage.EXAM_RESULT_SCORE_INVALID.message);
        }
        if (examResult.getResultDate() == null) {
            log.error("Exam result date null");
            throw new DataInvalidException(ExceptionMessage.EXAM_RESULT_DATE_INVALID.message);
        }
        if (examResult.getStudent() == null || examResult.getStudent().getId() == null) {
            log.error("Exam result student reference null");
            throw new ForeignKeyException(String.format(ExceptionMessage.NULL_FOREIGN_KEY_REFERENCE.message, "Student"));
        }
        if (examResult.getExam() == null || examResult.getExam().getId() == null) {
            log.error("Exam result exam reference null");
            throw new ForeignKeyException(String.format(ExceptionMessage.NULL_FOREIGN_KEY_REFERENCE.message, "Exam"));
        }
        if (examResult.getClazz() == null || examResult.getClazz().getId() == null) {
            log.error("Exam result class reference null");
            throw new ForeignKeyException(String.format(ExceptionMessage.NULL_FOREIGN_KEY_REFERENCE.message, "Class"));
        }
        Optional<ExamResult> examResultOptional = examResultRepository.findByStudentIdAndExamIdAndClazzId(examResult.getStudent().getId(), examResult.getExam().getId(), examResult.getClazz().getId());
        if (examResultOptional.isPresent() && !examResultOptional.get().getId().equals(examResult.getId())) {
            log.error("Exam result {} has exist", examResultOptional.get().getId());
            throw new ResourceConflictException(ExceptionMessage.EXAM_RESULT_CONFLICT.message);
        }

        try {
            return examResultRepository.save(examResult);
        } catch (DataIntegrityViolationException e) {
            SQLException ex = (SQLException) e.getRootCause();
            throw new ForeignKeyException(ServiceUtils.sqlExceptionMessageFormat(ex.getMessage()));
        }
    }

    public void deleteById(Long id) {
        try {
            examResultRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Exam result {} not found", id);
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EXAM_RESULT_NOT_FOUND.toString(), id));
        }
    }
}
