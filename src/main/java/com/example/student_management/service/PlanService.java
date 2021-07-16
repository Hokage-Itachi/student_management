package com.example.student_management.service;

import com.example.student_management.domain.Plan;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.repository.PlanRepository;
import com.example.student_management.utils.ServiceUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class PlanService {
    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<Plan> findAll(Specification<Plan> specification, Pageable pageable) {
        if (specification == null) {
            return planRepository.findAll(pageable).getContent();
        }
        return planRepository.findAll(specification, pageable).getContent();
    }

    public Plan findById(Long id) {
        if (id == null) {
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "Plan"));
        }
        Optional<Plan> planOptional = planRepository.findById(id);
        if (planOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PLAN_NOT_FOUND.toString(), id));
        }
        return planOptional.get();
    }

    public Plan save(Plan plan) {
        if (plan.getCourse() == null || plan.getCourse().getId() == null) {
            throw new ForeignKeyException(String.format(ExceptionMessage.NULL_FOREIGN_KEY_REFERENCE.message, "Course"));
        }
        try {
            return planRepository.save(plan);
        } catch (DataIntegrityViolationException e) {
            SQLException sqlException = (SQLException) e.getRootCause();
            throw new ResourceNotFoundException(ServiceUtils.sqlExceptionMessageFormat(sqlException.getMessage()));
        }
    }

    public void deleteById(Long id) {

        try {
            planRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PLAN_NOT_FOUND.toString(), id));
        }
    }
}
