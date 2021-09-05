package com.example.student_management.controller;

import com.example.student_management.converter.PlanConverter;
import com.example.student_management.domain.Plan;
import com.example.student_management.dto.PlanDto;
import com.example.student_management.service.PlanService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plans")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
})
public class PlanController {
    private final PlanService planService;
    private final PlanConverter planConverter;

    public PlanController(PlanService planService, PlanConverter planConverter) {
        this.planService = planService;
        this.planConverter = planConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_plans')")
    @Operation(summary = "Get list plan")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> getAllPlan(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Plan> specification = new CustomSpecificationBuilder<Plan>(ServiceUtils.getFilterParam(filter, Plan.class)).build();
        List<Plan> plans = planService.findAll(specification, pageable);
        List<PlanDto> planDtoList = plans.stream().map(planConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(planDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_view_plan_by_id', 'can_view_all_plans')")
    @Operation(summary = "Get plan by id")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanDto.class)))
    public ResponseEntity<Object> getPlanById(@PathVariable("id") Long id) {
        Plan plan = planService.findById(id);
        return new ResponseEntity<>(planConverter.toDto(plan), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_plan')")
    @Operation(summary = "Create plan")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanDto.class)))
    public ResponseEntity<Object> addPlan(@RequestBody PlanDto planDto) {
        Plan plan = planConverter.toEntity(planDto);
        plan.setId(null);
        Plan insertedPlan = planService.save(plan);
        return new ResponseEntity<>(planConverter.toDto(insertedPlan), HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_update_plan')")
    @Operation(summary = "Update plan")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanDto.class)))
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
    @Operation(summary = "Delete plan")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> deletePlan(@PathVariable("id") Long id) {
        planService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
