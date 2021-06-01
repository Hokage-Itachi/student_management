package com.example.student_management.repository;

import com.example.student_management.domain.Permistion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermistionRepository extends JpaRepository<Permistion, Long> {
}
