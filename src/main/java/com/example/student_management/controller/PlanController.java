package com.example.student_management.controller;

import com.example.student_management.converter.PlanConverter;
import com.example.student_management.domain.Course;
import com.example.student_management.domain.Plan;
import com.example.student_management.dto.PlanDto;
import com.example.student_management.request.PlanRequest;
import com.example.student_management.service.CourseService;
import com.example.student_management.service.PlanService;
import com.example.student_management.utils.ServiceUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    private final PlanService planService;
    private final PlanConverter planConverter;

    public PlanController(PlanService planService, PlanConverter planConverter) {
        this.planService = planService;
        this.planConverter = planConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_plans')")
    public ResponseEntity<Object> getAllPlan() {
        List<Plan> plans = planService.findAll();
        List<PlanDto> planDtoList = plans.stream().map(planConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(planDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_view_plan_by_id', 'can_view_all_plans')")
    public ResponseEntity<Object> getPlanById(@PathVariable("id") Long id) {
        Plan plan = planService.findById(id);
        return new ResponseEntity<>(planConverter.toDto(plan), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_plan')")
    public ResponseEntity<Object> addPlan(@RequestBody PlanDto planDto) {
        Plan plan = planConverter.toEntity(planDto);
        plan.setId(null);
        Plan insertedPlan = planService.save(plan);
        return new ResponseEntity<>(planConverter.toDto(insertedPlan), HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_update_plan')")
    public ResponseEntity<Object> updatePlan(@PathVariable("id") Long id, @RequestBody PlanDto planDto) {
        Plan updatedTarget = planService.findById(id);
        Plan updatedSource = planConverter.toEntity(planDto);
        updatedSource.setId(id);
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Plan updatedPlan = planService.save(updatedTarget);
        return new ResponseEntity<>(planConverter.toDto(updatedPlan), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_plan_by_id')")
    public ResponseEntity<Object> deletePlan(@PathVariable("id") Long id) {
        planService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
