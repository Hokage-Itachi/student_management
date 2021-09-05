package com.example.student_management.controller;

import com.example.student_management.converter.CourseConverter;
import com.example.student_management.domain.Course;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.CourseDto;
import com.example.student_management.service.CourseService;
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
@RequestMapping("/api/courses")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
})
public class CourseController {
    private final CourseService courseService;
    private final CourseConverter courseConverter;

    public CourseController(CourseService courseService, CourseConverter courseConverter) {
        this.courseService = courseService;
        this.courseConverter = courseConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_courses')")
    @Operation(summary = "Get list course")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> getAllCourses(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Course> specification = new CustomSpecificationBuilder<Course>(
                ServiceUtils.getFilterParam(filter, Course.class)).build();
        List<Course> courses = courseService.findAll(pageable, specification);
        List<CourseDto> courseDtoList = courses.stream().map(courseConverter::toDto).collect(Collectors.toList());
        log.info("Get {} courses successfully", courseDtoList.size());
        return new ResponseEntity<>(courseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by id")
    @PreAuthorize("hasAnyAuthority('can_view_course_by_id', 'can_view_all_courses')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDto.class)))
    public ResponseEntity<Object> getCourseById(@PathVariable("id") Long id) {
        Course course = courseService.findById(id);
        CourseDto courseDto = courseConverter.toDto(course);
        log.info("Get course {} successfully", id);
        return new ResponseEntity<>(courseDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_course')")
    @Operation(summary = "Create course")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDto.class)))
    public ResponseEntity<Object> addCourse(@RequestBody CourseDto courseDto) {
        Course course = courseConverter.toEntity(courseDto);
        course.setId(null);
        Course insertedCourse = courseService.save(course);
        log.info("Inserted course {}", insertedCourse.getId());
        return new ResponseEntity<>(courseConverter.toDto(insertedCourse), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_course')")
    @Operation(summary = "Update course")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDto.class)))
    public ResponseEntity<Object> updateCourse(@PathVariable("id") Long id, @RequestBody CourseDto courseDto) {
        Course updatedTarget = courseService.findById(id);
        Course updatedSource = courseConverter.toEntity(courseDto);
        updatedSource.setId(id);
        // Copy non-null value from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Course updatedCourse = courseService.save(updatedTarget);
        log.info("Updated course {}", updatedCourse);
        return new ResponseEntity<>(courseConverter.toDto(updatedCourse), HttpStatus.OK);

    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_course_by_id ')")
    @Operation(summary = "Delete course")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
