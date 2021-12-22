package com.example.student_management.repository;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long>, JpaSpecificationExecutor<Class> {
}
