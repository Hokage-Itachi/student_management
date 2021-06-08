package com.example.student_management.controller;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Course;
import com.example.student_management.domain.Teacher;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.converter.ClassConverter;
import com.example.student_management.request.ClassRequest;
import com.example.student_management.service.ClassService;
import com.example.student_management.service.CourseService;
import com.example.student_management.service.TeacherService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: create response object include message, status_code and data
@RestController
@RequestMapping("/api/courses/{courseId}/classes")
public class ClassController {
    private final ClassService classService;
    private final CourseService courseService;
    private final ClassConverter classConverter;
    private final TeacherService teacherService;

    public ClassController(ClassService classService, CourseService courseService, ClassConverter classConverter, TeacherService teacherService) {
        this.classService = classService;
        this.courseService = courseService;
        this.classConverter = classConverter;
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllClasses(@PathVariable("courseId") Long courseId) {
        Optional<Course> optionalCourse = courseService.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Class> classes = optionalCourse.get().getClasses();
        List<ClassDto> classDtoList = classes.stream().map(classConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(classDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("courseId") Long courseId, @PathVariable(value = "id") Long id) {
        Optional<Course> optionalCourse = courseService.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Class> classOptional = classService.findById(id);
        if (classOptional.isEmpty()) {

            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
        ClassDto classDto = classConverter.toDto(classOptional.get());
        return new ResponseEntity<>(classDto, HttpStatus.OK);
    }

    // TODO: create response object for post request
    @PostMapping
    public ResponseEntity<Object> addClass(@PathVariable("courseId") Long courseId, @RequestBody ClassRequest request) {
        Optional<Course> courseOptional = courseService.findById(courseId);
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Course course = courseOptional.get();
        Class clazz = classConverter.toEntity(request.getClazz());
        Optional<Teacher> teacherOptional = teacherService.findById(request.getTeacher_id());
        if (teacherOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        clazz.setCourse(course);
        clazz.setTeacher(teacherOptional.get());
        Class insertedClass = classService.save(clazz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateClass(@PathVariable("courseId") Long courseId, @PathVariable(value = "id") Long id, @RequestBody ClassRequest request) {
        Optional<Course> courseOptional = courseService.findById(courseId);
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Class> classOptional = classService.findById(id);
        if (classOptional.isEmpty()) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Teacher> teacherOptional = teacherService.findById(request.getTeacher_id());
        if (teacherOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Class clazz = classConverter.toEntity(request.getClazz());
        clazz.setId(id);
        clazz.setTeacher(teacherOptional.get());
        clazz.setCourse(courseOptional.get());
        classService.save(clazz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClass(@PathVariable("courseId") Long courseId, @PathVariable("id") Long id) {
        Optional<Course> courseOptional = courseService.findById(courseId);
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            classService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);


    }
}
