package com.example.student_management.controller;

import com.example.student_management.converter.CourseConverter;
import com.example.student_management.domain.Course;
import com.example.student_management.dto.CourseDto;
import com.example.student_management.service.CourseService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseConverter courseConverter;

    public CourseController(CourseService courseService, CourseConverter courseConverter) {
        this.courseService = courseService;
        this.courseConverter = courseConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_courses')")
    public ResponseEntity<Object> getAllCourses() {
        List<Course> courses = courseService.findAll();
        List<CourseDto> courseDtoList = courses.stream().map(courseConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(courseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_course_by_id')")
    public ResponseEntity<Object> getCourseById(@PathVariable("id") Long id) {
        Course course = courseService.findById(id);
        CourseDto courseDto = courseConverter.toDto(course);

        return new ResponseEntity<>(courseDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_course')")
    public ResponseEntity<Object> addCourse(@RequestBody CourseDto courseDto) {
        Course course = courseConverter.toEntity(courseDto);
        Course insertedCourse = courseService.save(course);
        return new ResponseEntity<>(courseConverter.toDto(insertedCourse), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_course')")
    public ResponseEntity<Object> updateCourse(@PathVariable("id") Long id, @RequestBody CourseDto courseDto) {
        Course course = courseService.findById(id);
        Course updatedCourseInfo = courseConverter.toEntity(courseDto);
        updatedCourseInfo.setId(id);
        Course updatedCourse = courseService.save(updatedCourseInfo);
        return new ResponseEntity<>(courseConverter.toDto(updatedCourse), HttpStatus.OK);

    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_course_by_id ')")
    public ResponseEntity<Object> deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
