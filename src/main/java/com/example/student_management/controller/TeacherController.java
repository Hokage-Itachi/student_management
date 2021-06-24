package com.example.student_management.controller;

import com.example.student_management.converter.TeacherConverter;
import com.example.student_management.domain.Teacher;
import com.example.student_management.dto.TeacherDto;
import com.example.student_management.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final TeacherConverter teacherConverter;


    public TeacherController(TeacherService teacherService, TeacherConverter teacherConverter) {
        this.teacherService = teacherService;
        this.teacherConverter = teacherConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_teachers')")
    public ResponseEntity<Object> getAllTeacher() {
        List<Teacher> teachers = teacherService.findAll();
        List<TeacherDto> teacherDtoList = teachers.stream().map(teacherConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(teacherDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_view_teacher_by_id','can_view_all_teachers')")
    public ResponseEntity<Object> getTeacherById(@PathVariable("id") Long id) {
        Teacher teacher = teacherService.findById(id);

        return new ResponseEntity<>(teacherConverter.toDto(teacher), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_teacher')")
    public ResponseEntity<Object> addTeacher(@RequestBody TeacherDto teacherDto) {
        // TODO: handle teacher's email duplicate
        Teacher teacher = teacherConverter.toEntity(teacherDto);
        Teacher insertedTeacher = teacherService.save(teacher);
        return new ResponseEntity<>(teacherConverter.toDto(insertedTeacher), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_update_teacher')")
    public ResponseEntity<Object> updateTeacher(@PathVariable("id") Long id, @RequestBody TeacherDto teacherDto) {
        Teacher teacher = teacherService.findById(id);
        // TODO: handle teacher's email duplicate
        Teacher teacherUpdateInfo = teacherConverter.toEntity(teacherDto);
        teacherUpdateInfo.setId(teacher.getId());

        Teacher updatedTeacher = teacherService.save(teacherUpdateInfo);
        return new ResponseEntity<>(teacherConverter.toDto(updatedTeacher), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_teacher_by_id')")
    public ResponseEntity<Object> deleteTeacher(@PathVariable("id") Long id) {
        teacherService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
