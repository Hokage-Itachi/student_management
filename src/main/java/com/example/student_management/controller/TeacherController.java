package com.example.student_management.controller;

import com.example.student_management.converter.TeacherConverter;
import com.example.student_management.domain.Teacher;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.TeacherDto;
import com.example.student_management.service.TeacherService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/teachers")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
})
public class TeacherController {
    private final TeacherService teacherService;
    private final TeacherConverter teacherConverter;


    public TeacherController(TeacherService teacherService, TeacherConverter teacherConverter) {
        this.teacherService = teacherService;
        this.teacherConverter = teacherConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_teachers')")
    @Operation(summary = "Get list teacher")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> getAllTeacher(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Teacher> specification = new CustomSpecificationBuilder<Teacher>(ServiceUtils.getFilterParam(filter, Teacher.class)).build();
        List<Teacher> teachers = teacherService.findAll(specification, pageable);
        List<TeacherDto> teacherDtoList = teachers.stream().map(teacherConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(teacherDtoList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_view_teacher_by_id','can_view_all_teachers')")
    @Operation(summary = "Get teacher by id")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDto.class)))
    public ResponseEntity<Object> getTeacherById(@PathVariable("id") Long id) {
        Teacher teacher = teacherService.findById(id);

        return new ResponseEntity<>(teacherConverter.toDto(teacher), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_teacher')")
    @Operation(summary = "Create teacher")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDto.class)))
    @ApiResponse(responseCode = "409", description = "Resource conflict", content = @Content)
    public ResponseEntity<Object> addTeacher(@RequestBody TeacherDto teacherDto) {
        Teacher teacher = teacherConverter.toEntity(teacherDto);
        teacher.setId(null);
        Teacher insertedTeacher = teacherService.save(teacher);
        return new ResponseEntity<>(teacherConverter.toDto(insertedTeacher), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_update_teacher')")
    @Operation(summary = "Update teacher")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDto.class)))
    @ApiResponse(responseCode = "409", description = "Resource conflict", content = @Content)
    public ResponseEntity<Object> updateTeacher(@PathVariable("id") Long id, @RequestBody TeacherDto teacherDto) {
        Teacher updatedTarget = teacherService.findById(id);
        Teacher updatedSource = teacherConverter.toEntity(teacherDto);
        updatedSource.setId(id);
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Teacher updatedTeacher = teacherService.save(updatedTarget);
        return new ResponseEntity<>(teacherConverter.toDto(updatedTeacher), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_teacher_by_id')")
    @Operation(summary = "Delete teacher")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> deleteTeacher(@PathVariable("id") Long id) {
        teacherService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
