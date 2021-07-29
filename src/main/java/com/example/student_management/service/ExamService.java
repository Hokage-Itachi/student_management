package com.example.student_management.service;

import com.example.student_management.domain.Exam;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.repository.ExamRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamService {
    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public List<Exam> findAll(Specification<Exam> specification, Pageable pageable) {
        if (specification == null) {
            return examRepository.findAll(pageable).getContent();
        }
        return examRepository.findAll(specification, pageable).getContent();
    }

    @Cacheable(value = "exam")
    public Exam findById(Long id) {
        if (id == null) {
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "Exam"));
        }
        Optional<Exam> examOptional = examRepository.findById(id);
        if (examOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EXAM_NOT_FOUND.toString(), id));
        }
        return examOptional.get();
    }

    @CachePut(value = "exam")
    public Exam save(Exam exam) {
        if (exam.getName() == null || exam.getName().isBlank()) {
            throw new DataInvalidException(ExceptionMessage.EXAM_NAME_INVALID.message);
        }
        return examRepository.save(exam);
    }

    @CacheEvict(value = "exam")
    public void deleteById(Long id) {
        try {
            examRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.EXAM_NOT_FOUND.message, id));
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyException(String.format(ExceptionMessage.EXAM_FOREIGN_KEY.message, id));
        }
    }
}
