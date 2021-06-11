package com.example.student_management.controller;

import com.example.student_management.converter.ExamResultConverter;
import com.example.student_management.domain.Class;
import com.example.student_management.domain.Exam;
import com.example.student_management.domain.ExamResult;
import com.example.student_management.domain.Student;
import com.example.student_management.dto.ExamResultDto;
import com.example.student_management.request.ExamResultRequest;
import com.example.student_management.service.ClassService;
import com.example.student_management.service.ExamResultService;
import com.example.student_management.service.ExamService;
import com.example.student_management.service.StudentService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/examResults")
public class ExamResultController {
    private final ExamResultConverter examResultConverter;
    private final ExamResultService examResultService;
    private final StudentService studentService;
    private final ExamService examService;
    private final ClassService classService;

    public ExamResultController(ExamResultConverter examResultConverter, ExamResultService examResultService, StudentService studentService, ExamService examService, ClassService classService) {
        this.examResultConverter = examResultConverter;
        this.examResultService = examResultService;
        this.studentService = studentService;
        this.examService = examService;
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllExamResults() {
        List<ExamResult> examResults = examResultService.findAll();
        List<ExamResultDto> examResultDtoList = examResults.stream().map(examResultConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(examResultDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getExamResultById(@PathVariable("id") Long id) {
        Optional<ExamResult> examResultOptional = examResultService.findById(id);
        if (examResultOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(examResultConverter.toDto(examResultOptional.get()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addExamResult(@RequestBody ExamResultRequest request) {
        Optional<Student> studentOptional = studentService.findById(request.getStudentId());
        if (studentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Class> classOptional = classService.findById(request.getClassId());
        if (classOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Exam> examOptional = examService.findById(request.getExamId());
        if (examOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ExamResult examResult = examResultConverter.toEntity(request.getExamResult());
        examResult.setExam(examOptional.get());
        examResult.setClazz(classOptional.get());
        examResult.setStudent(studentOptional.get());

        ExamResult insertedExamResult = examResultService.save(examResult);
        return new ResponseEntity<>(examResultConverter.toDto(insertedExamResult), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateExamResult(@PathVariable("id") Long id, @RequestBody ExamResultRequest request) {
        Optional<ExamResult> examResultOptional = examResultService.findById(id);
        if (examResultOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Student> studentOptional = studentService.findById(request.getStudentId());
        if (studentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Class> classOptional = classService.findById(request.getClassId());
        if (classOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Exam> examOptional = examService.findById(request.getExamId());
        if (examOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ExamResult examResult = examResultConverter.toEntity(request.getExamResult());
        examResult.setId(id);
        examResult.setExam(examOptional.get());
        examResult.setClazz(classOptional.get());
        examResult.setStudent(studentOptional.get());

        ExamResult updatedExamResult = examResultService.save(examResult);
        return new ResponseEntity<>(examResultConverter.toDto(updatedExamResult), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExamResult(@PathVariable("id") Long id) {
        try {
            examResultService.deleteById(id);
            ;
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
