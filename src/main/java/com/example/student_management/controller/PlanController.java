package com.example.student_management.controller;

import com.example.student_management.converter.PlanConverter;
import com.example.student_management.domain.Course;
import com.example.student_management.domain.Plan;
import com.example.student_management.dto.PlanDto;
import com.example.student_management.request.PlanRequest;
import com.example.student_management.service.CourseService;
import com.example.student_management.service.PlanService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    private final PlanService planService;
    private final PlanConverter planConverter;
    private final CourseService courseService;

    public PlanController(PlanService planService, PlanConverter planConverter, CourseService courseService) {
        this.planService = planService;
        this.planConverter = planConverter;
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_plans')")
    public ResponseEntity<Object> getAllPlan() {
        List<Plan> plans = planService.findAll();
        List<PlanDto> planDtoList = plans.stream().map(planConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(planDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_view_plan_by_id')")
    public ResponseEntity<Object> getPlanById(@PathVariable("id") Long id) {
        Optional<Plan> planOptional = planService.findById(id);
        if (planOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PlanDto plan = planConverter.toDto(planOptional.get());
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_plan')")
    public ResponseEntity<Object> addPlan(@RequestBody PlanRequest request) {
        Optional<Course> courseOptional = courseService.findById(request.getCourseId());
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Plan plan = planConverter.toEntity(request.getPlan());
        plan.setCourse(courseOptional.get());
        planService.save(plan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_update_plan')")
    public ResponseEntity<Object> updatePlan(@PathVariable("id") Long id, @RequestBody PlanRequest request) {
        Optional<Plan> planOptional = planService.findById(id);
        if (planOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Course> courseOptional = courseService.findById(request.getCourseId());
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Plan planUpdateInfo = planConverter.toEntity(request.getPlan());
        planUpdateInfo.setId(id);
        planUpdateInfo.setCourse(courseOptional.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_plan_by_id')")
    public ResponseEntity<Object> deletePlan(@PathVariable("id") Long id) {
        try {
            planService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
