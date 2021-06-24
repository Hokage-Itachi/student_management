package com.example.student_management.controller;

import com.example.student_management.converter.StudentConverter;
import com.example.student_management.domain.Student;
import com.example.student_management.dto.StudentDto;
import com.example.student_management.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentConverter studentConverter;
    private final StudentService studentService;

    public StudentController(StudentConverter studentConverter, StudentService studentService) {
        this.studentConverter = studentConverter;
        this.studentService = studentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_students')")
    public ResponseEntity<Object> getAllStudent() {
        List<Student> students = studentService.findAll();
        List<StudentDto> studentDtoList = students.stream().map(studentConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(studentDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_student_by_id','can_view_all_students')")
    public ResponseEntity<Object> getStudentById(@PathVariable("id") Long id) {
        Student student = studentService.findById(id);

        return new ResponseEntity<>(studentConverter.toDto(student), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_student')")
    public ResponseEntity<Object> addStudent(@RequestBody StudentDto studentDto) {
        // TODO: handle student duplicates email
        Student student = studentConverter.toEntity(studentDto);
        Student insertedStudent = studentService.save(student);
        return new ResponseEntity<>(studentConverter.toDto(insertedStudent), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_student')")
    public ResponseEntity<Object> updateStudent(@PathVariable("id") Long id, @RequestBody StudentDto studentDto) {
        Student student = studentService.findById(id);

        Student studentUpdateInfo = studentConverter.toEntity(studentDto);
        studentUpdateInfo.setId(id);
        Student updatedStudent = studentService.save(studentUpdateInfo);
        return new ResponseEntity<>(studentConverter.toDto(updatedStudent), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_student_by_id')")
    public ResponseEntity<Object> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
