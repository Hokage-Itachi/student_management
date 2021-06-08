package com.example.student_management.controller;

import com.example.student_management.converter.ExamConverter;
import com.example.student_management.domain.Course;
import com.example.student_management.domain.Exam;
import com.example.student_management.dto.ExamDto;
import com.example.student_management.request.ExamRequest;
import com.example.student_management.service.CourseService;
import com.example.student_management.service.ExamService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<Object> getAllExam() {
        List<Exam> exams = examService.findAll();
        List<ExamDto> examDtoList = exams.stream().map(examConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(examDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getExamById(@PathVariable("id") Long id) {
        Optional<Exam> examOptional = examService.findById(id);
        if (examOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ExamDto examDto = examConverter.toDto(examOptional.get());
        return new ResponseEntity<>(examDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addExam(@RequestBody ExamRequest request) {
        Optional<Course> courseOptional = courseService.findById(request.getCourseId());
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Exam exam = examConverter.toEntity(request.getExam());
        exam.setCourse(courseOptional.get());
        examService.save(exam);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateExam(@PathVariable("id") Long id, @RequestBody ExamRequest request) {
        Optional<Exam> examOptional = examService.findById(id);
        if (examOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Course> courseOptional = courseService.findById(request.getCourseId());
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Exam examUpdateInfo = examConverter.toEntity(request.getExam());
        examUpdateInfo.setId(id);
        examUpdateInfo.setCourse(courseOptional.get());

        examService.save(examUpdateInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletePlan(@PathVariable("id") Long id) {
        try {
            examService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
