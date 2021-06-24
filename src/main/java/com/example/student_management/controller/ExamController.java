package com.example.student_management.controller;

import com.example.student_management.converter.ExamConverter;
import com.example.student_management.domain.Course;
import com.example.student_management.domain.Exam;
import com.example.student_management.dto.ExamDto;
import com.example.student_management.request.ExamRequest;
import com.example.student_management.service.CourseService;
import com.example.student_management.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private final ExamService examService;
    private final ExamConverter examConverter;
    private final CourseService courseService;

    public ExamController(ExamService examService, ExamConverter examConverter, CourseService courseService) {
        this.examService = examService;
        this.examConverter = examConverter;
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_exams')")
    public ResponseEntity<Object> getAllExam() {
        List<Exam> exams = examService.findAll();
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
    public ResponseEntity<Object> addExam(@RequestBody ExamRequest request) {
        Course course = courseService.findById(request.getCourseId());

        Exam exam = examConverter.toEntity(request.getExam());
        exam.setCourse(course);
        Exam insertedExam = examService.save(exam);

        return new ResponseEntity<>(examConverter.toDto(insertedExam), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_update_exam')")
    public ResponseEntity<Object> updateExam(@PathVariable("id") Long id, @RequestBody ExamRequest request) {
        Exam exam = examService.findById(id);
        Course course = courseService.findById(request.getCourseId());
        Exam examUpdateInfo = examConverter.toEntity(request.getExam());
        examUpdateInfo.setId(exam.getId());
        examUpdateInfo.setCourse(course);

        Exam updatedExam = examService.save(examUpdateInfo);
        return new ResponseEntity<>(examConverter.toDto(updatedExam), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_exam_by_id')")
    public ResponseEntity<Object> deleteExam(@PathVariable("id") Long id) {
        examService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
