package com.example.student_management.controller;

import com.example.student_management.converter.ClassConverter;
import com.example.student_management.domain.Class;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.request.ClassFilterRequest;
import com.example.student_management.service.ClassService;
import com.example.student_management.specification.ClassSpecification;
import com.example.student_management.utils.ServiceUtils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
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
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound")
})
public class ClassController {
    private final ClassService classService;
    private final ClassConverter classConverter;

    public ClassController(ClassService classService, ClassConverter classConverter) {
        this.classService = classService;
        this.classConverter = classConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_classes')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> getAllClasses(
            @ParameterObject ClassFilterRequest filterRequest,
            @ParameterObject Pageable pageable
    ) {
        Specification<Class> specification = ClassSpecification.buildSpecification(filterRequest);
        List<Class> classes = classService.findAll(pageable, specification);
        List<ClassDto> classDtoList = classes.stream().map(classConverter::toDto).collect(Collectors.toList());
        classDtoList = classDtoList.stream().peek((classDto -> {
            classDto.setCourse(null);
            classDto.setTeacher(null);
        })).collect(Collectors.toList());
        log.info("Get {} classes successfully", classDtoList.size());
        return new ResponseEntity<>(classDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_all_classes', 'can_view_class_by_id')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassDto.class)))
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id) {
        ClassDto classDto = classConverter.toDto(classService.findById(id));
        log.info("Get class {} successfully", id);
        return new ResponseEntity<>(classDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_class')")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassDto.class)))
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    public ResponseEntity<Object> addClass(@RequestBody ClassDto classDto) {
        Class clazz = classConverter.toEntity(classDto);
        clazz.setId(null);
        Class insertedClass = classService.save(clazz);
        log.info("Inserted class {}", clazz.getId());
        return new ResponseEntity<>(classConverter.toDto(insertedClass), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_class')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassDto.class)))
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
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
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> deleteClass(@PathVariable("id") Long id) {
        classService.deleteById(id);
        log.info("Deleted class {}", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
