package com.example.student_management.controller;

import com.example.student_management.converter.StudentConverter;
import com.example.student_management.domain.Student;
import com.example.student_management.dto.StudentDto;
import com.example.student_management.service.StudentService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
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
@RequestMapping("/api/students")
@Slf4j
public class StudentController {
    private final StudentConverter studentConverter;
    private final StudentService studentService;

    public StudentController(StudentConverter studentConverter, StudentService studentService) {
        this.studentConverter = studentConverter;
        this.studentService = studentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_students')")
    public ResponseEntity<Object> getAllStudent(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Student> specification = new CustomSpecificationBuilder<Student>(ServiceUtils.getFilterParam(filter, Student.class)).build();
        List<Student> students = studentService.findAll(specification, pageable);
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
        Student student = studentConverter.toEntity(studentDto);
        student.setId(null);
        Student insertedStudent = studentService.save(student);
        return new ResponseEntity<>(studentConverter.toDto(insertedStudent), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_student')")
    public ResponseEntity<Object> updateStudent(@PathVariable("id") Long id, @RequestBody StudentDto studentDto) {
        Student updatedTarget = studentService.findById(id);
        Student updatedSource = studentConverter.toEntity(studentDto);
        updatedSource.setId(id);
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Student updatedStudent = studentService.save(updatedTarget);
        return new ResponseEntity<>(studentConverter.toDto(updatedStudent), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_student_by_id')")
    public ResponseEntity<Object> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
