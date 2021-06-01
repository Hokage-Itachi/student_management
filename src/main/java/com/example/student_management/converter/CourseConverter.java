package com.example.student_management.converter;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Course;
import com.example.student_management.dto.CourseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseConverter {
    public CourseDto toDto(Course entity) {
        List<String> classes = entity.getClasses().stream().map(Class::getName).collect(Collectors.toList());
        CourseDto courseDto = CourseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .createdDate(entity.getCreateDate())
                .classes(classes)
                .build();
        return courseDto;
    }

    public Course toEntity(CourseDto courseDto) {
        return Course.builder()
                .name(courseDto.getName())
                .type(courseDto.getType())
                .createDate(courseDto.getCreatedDate())
                .build();
    }
}
