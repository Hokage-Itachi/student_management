package com.example.student_management.service;

import com.example.student_management.domain.Course;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.repository.CourseRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll(Pageable pageable, Specification<Course> specification) {
        if (specification == null) {
            return courseRepository.findAll(pageable).getContent();
        }
        return courseRepository.findAll(specification, pageable).getContent();
    }

    @Cacheable(value = "course")
    public Course findById(Long id) {
        if (id == null) {
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "Course"));
        }
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.COURSE_NOT_FOUND.toString(), id));
        }
        return courseOptional.get();
    }

    @CachePut(value = "course")
    public Course save(Course course) {
        if (course.getName() == null || course.getName().isBlank()) {
            throw new DataInvalidException(ExceptionMessage.COURSE_NAME_INVALID.message);
        }
        if (course.getCreateDate() == null) {
            throw new DataInvalidException(ExceptionMessage.CREATE_DATE_INVALID.message);
        }
        return courseRepository.save(course);
    }

    @CacheEvict(value = "course")
    public void deleteById(Long id) {
        try {
            courseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.COURSE_NOT_FOUND.toString(), id));
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyException(String.format(ExceptionMessage.COURSE_FOREIGN_KEY.message, id));
        }
    }


}
