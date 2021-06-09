package com.example.student_management.controller;

import com.example.student_management.converter.TeacherConverter;
import com.example.student_management.domain.Teacher;
import com.example.student_management.dto.TeacherDto;
import com.example.student_management.service.TeacherService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<Object> getAllTeacher() {
        List<Teacher> teachers = teacherService.findAll();
        List<TeacherDto> teacherDtoList = teachers.stream().map(teacherConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(teacherDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getTeacherById(@PathVariable("id") Long id) {
        Optional<Teacher> teacherOptional = teacherService.findById(id);
        if (teacherOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TeacherDto teacher = teacherConverter.toDto(teacherOptional.get());
        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addTeacher(@RequestBody TeacherDto teacherDto) {
        Optional<Teacher> teacherOptional = teacherService.findByEmail(teacherDto.getEmail());
        if (teacherOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Teacher teacher = teacherConverter.toEntity(teacherDto);
        Teacher insertedTeacher = teacherService.save(teacher);
        return new ResponseEntity<>(insertedTeacher, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTeacher(@PathVariable("id") Long id, @RequestBody TeacherDto teacherDto) {
        Optional<Teacher> teacherOptional = teacherService.findById(id);
        if (teacherOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Teacher teacherUpdateInfo = teacherConverter.toEntity(teacherDto);
        if (!teacherOptional.get().getEmail().equals(teacherUpdateInfo.getEmail()) && teacherService.findByEmail(teacherDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        teacherUpdateInfo.setId(id);

        Teacher updatedTeacher = teacherService.save(teacherUpdateInfo);
        return new ResponseEntity<>(updatedTeacher, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTeacher(@PathVariable("id") Long id) {
        try {
            teacherService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
