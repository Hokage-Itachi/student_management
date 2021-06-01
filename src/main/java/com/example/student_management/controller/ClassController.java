package com.example.student_management.controller;

import com.example.student_management.domain.Class;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.converter.ClassConverter;
import com.example.student_management.service.ClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
    private final ClassService classService;
    private final ClassConverter classConverter;

    public ClassController(ClassService classService, ClassConverter classConverter) {
        this.classService = classService;
        this.classConverter = classConverter;
    }

    @GetMapping
    public ResponseEntity<Object> getAllClasses() {
        List<Class> classes = classService.findAll();
        List<ClassDto> classDtoList = classes.stream().map(classConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(classDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id) {
        Optional<Class> classOptional = classService.findById(id);
        if (classOptional.isEmpty()) {
            // TODO: create response object include message, status_code and data
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
        ClassDto classDto = classConverter.toDto(classOptional.get());
        return new ResponseEntity<>(classDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addClass(@RequestBody ClassDto classDto) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
