package com.example.student_management.controller;

import com.example.student_management.converter.ExamResultConverter;
import com.example.student_management.domain.ExamResult;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.ExamResultDto;
import com.example.student_management.service.ExamResultService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/examResults")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
})
public class ExamResultController {
    private final ExamResultConverter examResultConverter;
    private final ExamResultService examResultService;

    public ExamResultController(ExamResultConverter examResultConverter, ExamResultService examResultService) {
        this.examResultConverter = examResultConverter;
        this.examResultService = examResultService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_exam_results')")
    @Operation(summary = "Get list exam result")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> getAllExamResults(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<ExamResult> specification = new CustomSpecificationBuilder<ExamResult>(ServiceUtils.getFilterParam(filter, ExamResult.class)).build();
        List<ExamResult> examResults = examResultService.findAll(specification, pageable);
        List<ExamResultDto> examResultDtoList = examResults.stream().map(examResultConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(examResultDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_exam_result_by_id', 'can_view_all_exam_results')")
    @Operation(summary = "Get exam result by id")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExamResultDto.class)))
    public ResponseEntity<Object> getExamResultById(@PathVariable("id") Long id) {
        ExamResult examResult = examResultService.findById(id);

        return new ResponseEntity<>(examResultConverter.toDto(examResult), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_exam_result')")
    @Operation(summary = "Create exam result")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExamResultDto.class)))
    public ResponseEntity<Object> addExamResult(@RequestBody ExamResultDto examResultDto) {
        ExamResult examResult = examResultConverter.toEntity(examResultDto);
        examResult.setId(null);
        ExamResult insertedExamResult = examResultService.save(examResult);
        log.info("Exam result {} inserted", insertedExamResult);
        return new ResponseEntity<>(examResultConverter.toDto(insertedExamResult), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_exam_result')")
    @Operation(summary = "Update exam result")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExamResultDto.class)))
    public ResponseEntity<Object> updateExamResult(@PathVariable("id") Long id, @RequestBody ExamResultDto examResultDto) {
        ExamResult updatedTarget = examResultService.findById(id);
        ExamResult updatedSource = examResultConverter.toEntity(examResultDto);
        updatedSource.setId(id);
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        ExamResult updatedExamResult = examResultService.save(updatedTarget);
        log.info("Exam result {} updated", id);
        return new ResponseEntity<>(examResultConverter.toDto(updatedExamResult), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_exam_result_by_id')")
    @Operation(summary = "Delete exam result")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> deleteExamResult(@PathVariable("id") Long id) {
        examResultService.deleteById(id);
        log.info("Exam result {} deleted", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
