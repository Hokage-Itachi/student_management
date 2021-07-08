package com.example.student_management.repository;

import com.example.student_management.domain.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    Optional<ExamResult> findByStudentIdAndExamIdAndClazzId(Long studentId, Long examId, Long classId);
}
