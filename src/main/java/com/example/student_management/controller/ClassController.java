package com.example.student_management.controller;

import com.example.student_management.converter.ClassConverter;
import com.example.student_management.domain.Class;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/classes")
@Slf4j
public class ClassController {
    private final ClassService classService;
    private final ClassConverter classConverter;

    public ClassController(ClassService classService, ClassConverter classConverter) {
        this.classService = classService;
        this.classConverter = classConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_classes')")
    public ResponseEntity<Object> getAllClasses() {

        List<Class> classes = classService.findAll();
        List<ClassDto> classDtoList = classes.stream().map(classConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(classDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_all_classes', 'can_view_class_by_id')")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id) {
        ClassDto classDto = classConverter.toDto(classService.findById(id));
        return new ResponseEntity<>(classDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_class')")
    public ResponseEntity<Object> addClass(@RequestBody ClassDto classDto) {

        Class clazz = classConverter.toEntity(classDto);
        clazz.setId(null);
        Class insertedClass = classService.save(clazz);
        log.info("{} inserted", clazz);
        return new ResponseEntity<>(classConverter.toDto(insertedClass), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_class')")
    public ResponseEntity<Object> updateClass(@PathVariable(value = "id") Long id, @RequestBody ClassDto classDto) {
        classService.findById(id);
        Class clazz = classConverter.toEntity(classDto);
        clazz.setId(id);
        Class updatedClass = classService.save(clazz);
        log.info("{} updated", updatedClass);
        return new ResponseEntity<>(classConverter.toDto(updatedClass), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_class_by_id')")
    public ResponseEntity<Object> deleteClass(@PathVariable("id") Long id) {
        classService.deleteById(id);
        log.info("Class {} deleted", id);
        return new ResponseEntity<>(HttpStatus.OK);


    }
}
