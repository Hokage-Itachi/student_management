package com.example.student_management.converter;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Course;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.CourseDto;
import com.example.student_management.dto.ExamDto;
import com.example.student_management.dto.PlanDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseConverter {

    private final ClassConverter classConverter;
    private final ExamConverter examConverter;
    private final PlanConverter planConverter;

    public CourseConverter(ClassConverter classConverter, ExamConverter examConverter, PlanConverter planConverter) {
        this.classConverter = classConverter;
        this.examConverter = examConverter;
        this.planConverter = planConverter;
    }

    public CourseDto toDto(Course entity) {
        List<ClassDto> classes = entity.getClasses().stream().map(classConverter::toDto).collect(Collectors.toList());
        List<ExamDto> exams = entity.getExams().stream().map(examConverter::toDto).collect(Collectors.toList());
        List<PlanDto> plans = entity.getPlans().stream().map(planConverter::toDto).collect(Collectors.toList());
        CourseDto courseDto = CourseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .createdDate(entity.getCreateDate())
                .classes(classes)
                .exams(exams)
                .plans(plans)
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
