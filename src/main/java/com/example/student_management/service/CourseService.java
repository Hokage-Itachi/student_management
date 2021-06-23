package com.example.student_management.service;

import com.example.student_management.domain.Course;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.CourseRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.COURSE_NOT_FOUND.toString(), id));
        }
        return courseOptional.get();
    }


    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void deleteById(Long id) {
        try {
            courseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.COURSE_NOT_FOUND.toString(), id));
        }
    }


}
