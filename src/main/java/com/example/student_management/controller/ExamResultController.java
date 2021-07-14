package com.example.student_management.controller;

import com.example.student_management.converter.ExamResultConverter;
import com.example.student_management.domain.ExamResult;
import com.example.student_management.dto.ExamResultDto;
import com.example.student_management.service.ExamResultService;
import com.example.student_management.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.BeanIds;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/examResults")
@Slf4j
public class ExamResultController {
    private final ExamResultConverter examResultConverter;
    private final ExamResultService examResultService;

    public ExamResultController(ExamResultConverter examResultConverter, ExamResultService examResultService) {
        this.examResultConverter = examResultConverter;
        this.examResultService = examResultService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_exam_results')")
    public ResponseEntity<Object> getAllExamResults() {
        List<ExamResult> examResults = examResultService.findAll();
        List<ExamResultDto> examResultDtoList = examResults.stream().map(examResultConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(examResultDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_exam_result_by_id', 'can_view_all_exam_results')")
    public ResponseEntity<Object> getExamResultById(@PathVariable("id") Long id) {
        ExamResult examResult = examResultService.findById(id);

        return new ResponseEntity<>(examResultConverter.toDto(examResult), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_exam_result')")
    public ResponseEntity<Object> addExamResult(@RequestBody ExamResultDto examResultDto) {
        ExamResult examResult = examResultConverter.toEntity(examResultDto);
        examResult.setId(null);
        ExamResult insertedExamResult = examResultService.save(examResult);
        log.info("Exam result {} inserted", insertedExamResult);
        return new ResponseEntity<>(examResultConverter.toDto(insertedExamResult), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_exam_result')")
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
    public ResponseEntity<Object> deleteExamResult(@PathVariable("id") Long id) {
        examResultService.deleteById(id);
        log.info("Exam result {} deleted", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
