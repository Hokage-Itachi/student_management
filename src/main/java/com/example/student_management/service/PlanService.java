package com.example.student_management.service;

import com.example.student_management.domain.Plan;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.PlanRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {
    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    public Plan findById(Long id) {
        if (id == null){
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "Plan"));
        }
        Optional<Plan> planOptional = planRepository.findById(id);
        if (planOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PLAN_NOT_FOUND.toString(), id));
        }
        return planOptional.get();
    }

    public Plan save(Plan plan) {
        return planRepository.save(plan);
    }

    public void deleteById(Long id) {

        try {
            planRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PLAN_NOT_FOUND.toString(), id));
        }
    }
}
