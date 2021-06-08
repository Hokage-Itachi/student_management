package com.example.student_management.controller;

import com.example.student_management.converter.CourseConverter;
import com.example.student_management.domain.Course;
import com.example.student_management.dto.CourseDto;
import com.example.student_management.service.CourseService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> getAllCourses() {
        List<Course> courses = courseService.findAll();
        List<CourseDto> courseDtoList = courses.stream().map(courseConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(courseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCourseById(@PathVariable("id") Long id) {
        Optional<Course> courseOptional = courseService.findById(id);
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CourseDto courseDto = courseConverter.toDto(courseOptional.get());

        return new ResponseEntity<>(courseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addCourse(@RequestBody CourseDto courseDto) {
        Course course = courseConverter.toEntity(courseDto);
        Course savedCourse = courseService.save(course);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCourse(@PathVariable("id") Long id, @RequestBody CourseDto courseDto) {
        Optional<Course> courseOptional = courseService.findById(id);

        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Course updatedCourseInfo = courseConverter.toEntity(courseDto);
        updatedCourseInfo.setId(id);

        return new ResponseEntity<>(courseService.save(updatedCourseInfo), HttpStatus.OK);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable("id") Long id) {
        try {
            courseService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
