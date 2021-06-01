package com.example.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamResult extends JpaRepository<ExamResult, Long> {
}
