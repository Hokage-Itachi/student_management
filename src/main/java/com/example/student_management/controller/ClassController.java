package com.example.student_management.controller;

import com.example.student_management.converter.ClassConverter;
import com.example.student_management.converter.StudentConverter;
import com.example.student_management.domain.Class;
import com.example.student_management.domain.Course;
import com.example.student_management.domain.Student;
import com.example.student_management.domain.Teacher;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.StudentDto;
import com.example.student_management.request.ClassRequest;
import com.example.student_management.service.ClassService;
import com.example.student_management.service.CourseService;
import com.example.student_management.service.StudentService;
import com.example.student_management.service.TeacherService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/classes")
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
    @PreAuthorize("hasAnyAuthority('can_view_all_classes')")
    public ResponseEntity<Object> getAllClasses() {

        List<Class> classes = classService.findAll();
        List<ClassDto> classDtoList = classes.stream().map(classConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(classDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_all_classes', 'can_view_class_by_id')")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id) {
        Optional<Class> classOptional = classService.findById(id);
        if (classOptional.isEmpty()) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ClassDto classDto = classConverter.toDto(classOptional.get());
        return new ResponseEntity<>(classDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_class')")
    public ResponseEntity<Object> addClass(@RequestBody ClassRequest request) {

        Class clazz = classConverter.toEntity(request.getClazz());
        Optional<Teacher> teacherOptional = teacherService.findById(request.getTeacherId());
        if (teacherOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Course> courseOptional = courseService.findById(request.getCourseId());
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        clazz.setCourse(courseOptional.get());
        clazz.setTeacher(teacherOptional.get());
        Class insertedClass = classService.save(clazz);
        return new ResponseEntity<>(classConverter.toDto(insertedClass), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_class')")
    public ResponseEntity<Object> updateClass(@PathVariable(value = "id") Long id, @RequestBody ClassRequest request) {
        Optional<Class> classOptional = classService.findById(id);
        if (classOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Teacher> teacherOptional = teacherService.findById(request.getTeacherId());
        if (teacherOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Course> courseOptional = courseService.findById(request.getCourseId());
        if (courseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Class clazz = classConverter.toEntity(request.getClazz());
        clazz.setId(id);
        clazz.setTeacher(teacherOptional.get());
        clazz.setCourse(courseOptional.get());

        Class updatedClass = classService.save(clazz);
        return new ResponseEntity<>(classConverter.toDto(updatedClass), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_class')")
    public ResponseEntity<Object> deleteClass(@PathVariable("id") Long id) {
        try {
            classService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);


    }
}
