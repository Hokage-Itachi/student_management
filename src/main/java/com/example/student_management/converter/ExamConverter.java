package com.example.student_management.converter;

import com.example.student_management.domain.Exam;
import com.example.student_management.dto.ExamDto;
import org.springframework.stereotype.Component;

@Component
public class ExamConverter {
    private final CourseConverter courseConverter;

    public ExamConverter(CourseConverter courseConverter) {
        this.courseConverter = courseConverter;
    }

    public ExamDto toDto(Exam entity) {
        if(entity == null){
            return null;
        }
        return ExamDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .course(courseConverter.toDto(entity.getCourse()))
                .build();
    }

    public Exam toEntity(ExamDto examDto) {
        if (examDto == null) {
            return null;
        }
        return Exam.builder()
                .id(examDto.getId())
                .name(examDto.getName())
                .course(courseConverter.toEntity(examDto.getCourse()))
                .build();
    }
}
