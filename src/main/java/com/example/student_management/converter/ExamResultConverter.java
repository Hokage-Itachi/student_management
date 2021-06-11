package com.example.student_management.converter;

import com.example.student_management.domain.ExamResult;
import com.example.student_management.dto.ExamResultDto;
import org.springframework.stereotype.Component;

@Component
public class ExamResultConverter {
    public ExamResultDto toDto(ExamResult entity) {
        return ExamResultDto.builder()
                .id(entity.getId())
                .score(entity.getScore())
                .resultDate(entity.getResultDate())
                .note(entity.getNote())
                .student(entity.getStudent().getFullName())
                .exam(entity.getExam().getName())
                .clazz(entity.getClazz().getName())
                .build();
    }

    public ExamResult toEntity(ExamResultDto examResultDto) {
        return ExamResult.builder()
                .id(examResultDto.getId())
                .score(examResultDto.getScore())
                .resultDate(examResultDto.getResultDate())
                .note(examResultDto.getNote())
                .build();
    }
}
