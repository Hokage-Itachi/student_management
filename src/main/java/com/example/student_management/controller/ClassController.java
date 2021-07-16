package com.example.student_management.controller;

import com.example.student_management.converter.ClassConverter;
import com.example.student_management.domain.Class;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.service.ClassService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
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
    public ResponseEntity<Object> getAllClasses(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Class> spec = new CustomSpecificationBuilder<Class>(ServiceUtils.getFilterParam(filter, Class.class)).build();
        List<Class> classes = classService.findAll(pageable, spec);
        List<ClassDto> classDtoList = classes.stream().map(classConverter::toDto).collect(Collectors.toList());
        log.info("Get {} classes successfully", classDtoList.size());
        return new ResponseEntity<>(classDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_all_classes', 'can_view_class_by_id')")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id) {
        ClassDto classDto = classConverter.toDto(classService.findById(id));
        log.info("Get {} class successfully", id);
        return new ResponseEntity<>(classDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_class')")
    public ResponseEntity<Object> addClass(@RequestBody ClassDto classDto) {
        Class clazz = classConverter.toEntity(classDto);
        clazz.setId(null);
        Class insertedClass = classService.save(clazz);
        log.info("Inserted class {}", clazz.getId());
        return new ResponseEntity<>(classConverter.toDto(insertedClass), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_class')")
    public ResponseEntity<Object> updateClass(@PathVariable(value = "id") Long id, @RequestBody ClassDto classDto) {
        Class updatedTarget = classService.findById(id);
        Class updatedSource = classConverter.toEntity(classDto);
        updatedSource.setId(id);
        // Copy non-null value from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Class updatedClass = classService.save(updatedTarget);
        log.info("Updated class {}", id);
        return new ResponseEntity<>(classConverter.toDto(updatedClass), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_class_by_id')")
    public ResponseEntity<Object> deleteClass(@PathVariable("id") Long id) {
        classService.deleteById(id);
        log.info("Deleted class {}", id);
        return new ResponseEntity<>(HttpStatus.OK);


    }
}
