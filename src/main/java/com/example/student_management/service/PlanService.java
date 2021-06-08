package com.example.student_management.service;

import com.example.student_management.domain.Plan;
import com.example.student_management.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {
    private PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    public Optional<Plan> findById(Long id) {
        return planRepository.findById(id);
    }

    public Plan save(Plan plan) {
        return planRepository.save(plan);
    }

    public void deleteById(Long id) {
        planRepository.deleteById(id);
    }
}
