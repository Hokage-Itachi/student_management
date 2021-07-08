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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Object> addExamResult(@RequestBody ExamResultRequest request) {
        Student student = studentService.findById(request.getStudentId());
        Class clazz = classService.findById(request.getClassId());
        Exam exam = examService.findById(request.getExamId());

        ExamResult examResult = examResultConverter.toEntity(request.getExamResult());
        examResult.setId(null);
        examResult.setExam(exam);
        examResult.setClazz(clazz);
        examResult.setStudent(student);

        ExamResult insertedExamResult = examResultService.add(examResult);
        return new ResponseEntity<>(examResultConverter.toDto(insertedExamResult), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_exam_result')")
    public ResponseEntity<Object> updateExamResult(@PathVariable("id") Long id, @RequestBody ExamResultRequest request) {
        examResultService.findById(id);
        Student student = studentService.findById(request.getStudentId());
        Class clazz = classService.findById(request.getClassId());
        Exam exam = examService.findById(request.getExamId());

        ExamResult examResult = examResultConverter.toEntity(request.getExamResult());
        examResult.setId(id);
        examResult.setExam(exam);
        examResult.setClazz(clazz);
        examResult.setStudent(student);

        ExamResult updatedExamResult = examResultService.update(examResult);
        return new ResponseEntity<>(examResultConverter.toDto(updatedExamResult), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_exam_result_by_id')")
    public ResponseEntity<Object> deleteExamResult(@PathVariable("id") Long id) {
        examResultService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
