package com.example.student_management.controller;

import com.example.student_management.converter.ExamConverter;
import com.example.student_management.domain.Exam;
import com.example.student_management.dto.ExamDto;
import com.example.student_management.service.ExamService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
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
@RequestMapping("/api/exams")
@Slf4j
public class ExamController {
    private final ExamService examService;
    private final ExamConverter examConverter;

    public ExamController(ExamService examService, ExamConverter examConverter) {
        this.examService = examService;
        this.examConverter = examConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_exams')")
    public ResponseEntity<Object> getAllExam(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Exam> specification = new CustomSpecificationBuilder<Exam>(ServiceUtils.getFilterParam(filter, Exam.class)).build();
        List<Exam> exams = examService.findAll(specification, pageable);
        List<ExamDto> examDtoList = exams.stream().map(examConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(examDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_view_exam_by_id', 'can_view_all_exams')")
    public ResponseEntity<Object> getExamById(@PathVariable("id") Long id) {
        Exam exam = examService.findById(id);
        ExamDto examDto = examConverter.toDto(exam);
        return new ResponseEntity<>(examDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_exam')")
    public ResponseEntity<Object> addExam(@RequestBody ExamDto examDto) {
        Exam exam = examConverter.toEntity(examDto);
        exam.setId(null);
        Exam insertedExam = examService.save(exam);
        return new ResponseEntity<>(examConverter.toDto(insertedExam), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_update_exam')")
    public ResponseEntity<Object> updateExam(@PathVariable("id") Long id, @RequestBody ExamDto examDto) {
        Exam updatedTarget = examService.findById(id);
        Exam updatedSource = examConverter.toEntity(examDto);
        updatedSource.setId(id);
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Exam updatedExam = examService.save(updatedTarget);
        return new ResponseEntity<>(examConverter.toDto(updatedExam), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_exam_by_id')")
    public ResponseEntity<Object> deleteExam(@PathVariable("id") Long id) {
        examService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
