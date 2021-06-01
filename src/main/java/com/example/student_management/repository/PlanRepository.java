package com.example.student_management.repository;

import com.example.student_management.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
}
