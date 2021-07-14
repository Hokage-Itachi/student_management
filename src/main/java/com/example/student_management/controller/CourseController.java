package com.example.student_management.controller;

import com.example.student_management.converter.CourseConverter;
import com.example.student_management.domain.Course;
import com.example.student_management.dto.CourseDto;
import com.example.student_management.service.CourseService;
import com.example.student_management.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@Slf4j
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
        log.info("Get {} courses successfully", courseDtoList.size());
        return new ResponseEntity<>(courseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_course_by_id', 'can_view_all_courses')")
    public ResponseEntity<Object> getCourseById(@PathVariable("id") Long id) {
        Course course = courseService.findById(id);
        CourseDto courseDto = courseConverter.toDto(course);
        log.info("Get course {} successfully", id);
        return new ResponseEntity<>(courseDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_course')")
    public ResponseEntity<Object> addCourse(@RequestBody CourseDto courseDto) {
        Course course = courseConverter.toEntity(courseDto);
        course.setId(null);
        Course insertedCourse = courseService.save(course);
        log.info("Inserted course {}", insertedCourse.getId());
        return new ResponseEntity<>(courseConverter.toDto(insertedCourse), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_course')")
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
    public ResponseEntity<Object> deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
