package com.example.student_management.controller;

import com.example.student_management.converter.ClassConverter;
import com.example.student_management.converter.StudentConverter;
import com.example.student_management.domain.Student;
import com.example.student_management.dto.StudentDto;
import com.example.student_management.service.ClassService;
import com.example.student_management.service.StudentService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentConverter studentConverter;
    private final StudentService studentService;
    private final ClassConverter classConverter;
    private final ClassService classService;

    public StudentController(StudentConverter studentConverter, StudentService studentService, ClassConverter classConverter, ClassService classService) {
        this.studentConverter = studentConverter;
        this.studentService = studentService;
        this.classConverter = classConverter;
        this.classService = classService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_students')")
    public ResponseEntity<Object> getAllStudent() {
        List<Student> students = studentService.findAll();
        List<StudentDto> studentDtoList = students.stream().map(studentConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(studentDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_student_by_id')")
    public ResponseEntity<Object> getStudentById(@PathVariable("id") Long id) {
        Optional<Student> studentOptional = studentService.findById(id);
        if (studentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(studentConverter.toDto(studentOptional.get()), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_student')")
    public ResponseEntity<Object> addStudent(@RequestBody StudentDto studentDto) {
        Student student = studentConverter.toEntity(studentDto);
        Student insertedStudent = studentService.save(student);
        return new ResponseEntity<>(studentConverter.toDto(insertedStudent), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_student')")
    public ResponseEntity<Object> updateStudent(@PathVariable("id") Long id, @RequestBody StudentDto studentDto) {
        Optional<Student> optionalStudent = studentService.findById(id);
        if (optionalStudent.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Student studentUpdateInfo = studentConverter.toEntity(studentDto);
        studentUpdateInfo.setId(id);
        Student updatedStudent = studentService.save(studentUpdateInfo);
        return new ResponseEntity<>(studentConverter.toDto(updatedStudent), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_student_by_id')")
    public ResponseEntity<Object> deleteStudent(@PathVariable("id") Long id) {
        try {
            studentService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
