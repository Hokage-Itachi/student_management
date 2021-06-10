package com.example.student_management.repository;

import com.example.student_management.domain.Registration;
import com.example.student_management.domain.RegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, RegistrationId> {
}
