package com.example.student_management.converter;

import com.example.student_management.domain.Course;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.CourseDto;
import com.example.student_management.dto.ExamDto;
import com.example.student_management.dto.PlanDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseConverter {


    public CourseDto toDto(Course entity) {
        if(entity == null){
            return null;
        }
        return CourseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .createDate(entity.getCreateDate())
                .build();

    }

    public Course toEntity(CourseDto courseDto) {
        if (courseDto == null) {
            return null;
        }
        return Course.builder()
                .id(courseDto.getId())
                .name(courseDto.getName())
                .type(courseDto.getType())
                .createDate(courseDto.getCreateDate())
                .build();
    }
}
