package com.example.student_management.converter;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Event;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.CourseDto;
import com.example.student_management.dto.TeacherDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassConverter {

    private TeacherConverter teacherConverter;
    private CourseConverter courseConverter;

    public ClassConverter(TeacherConverter teacherConverter, CourseConverter courseConverter) {
        this.teacherConverter = teacherConverter;
        this.courseConverter = courseConverter;
    }

    public ClassDto toDto(Class entity) {
        TeacherDto teacher = teacherConverter.toDto(entity.getTeacher());
        List<String> events = entity.getEvents().stream().map(Event::getName).collect(Collectors.toList());
        CourseDto course = courseConverter.toDto(entity.getCourse());
        ClassDto classDto = ClassDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .teacher(teacher)
                .course(course)
                .events(events)
                .build();
        return classDto;
    }

    public Class toEntity(ClassDto classDto) {
        return new Class();
    }
}
