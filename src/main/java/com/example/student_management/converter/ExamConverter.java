package com.example.student_management.converter;

import com.example.student_management.domain.Exam;
import com.example.student_management.domain.ExamResult;
import com.example.student_management.dto.ExamDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExamConverter {
    public ExamDto toDto(Exam entity){

        ExamDto examDto = ExamDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .course(entity.getCourse().getName())
                .build();
        return examDto;
    }

    public Exam toEntity(ExamDto examDto){
        return Exam.builder()
                .id(examDto.getId())
                .name(examDto.getName())
                .build();
    }
}
