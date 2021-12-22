package com.example.student_management.converter;

import com.example.student_management.domain.ExamResult;
import com.example.student_management.dto.ExamResultDto;
import org.springframework.stereotype.Component;

@Component
public class ExamResultConverter {
    private final StudentConverter studentConverter;
    private final ExamConverter examConverter;
    private final ClassConverter classConverter;

    public ExamResultConverter(StudentConverter studentConverter, ExamConverter examConverter, ClassConverter classConverter) {
        this.studentConverter = studentConverter;
        this.examConverter = examConverter;
        this.classConverter = classConverter;
    }

    public ExamResultDto toDto(ExamResult entity) {
        if(entity == null){
            return null;
        }
        return ExamResultDto.builder()
                .id(entity.getId())
                .score(entity.getScore())
                .resultDate(entity.getResultDate())
                .note(entity.getNote())
                .student(studentConverter.toDto(entity.getStudent()))
                .exam(examConverter.toDto(entity.getExam()))
                .clazz(classConverter.toDto(entity.getClazz()))
                .build();
    }

    public ExamResult toEntity(ExamResultDto examResultDto) {
        if(examResultDto == null){
            return null;
        }
        return ExamResult.builder()
                .id(examResultDto.getId())
                .score(examResultDto.getScore())
                .resultDate(examResultDto.getResultDate())
                .note(examResultDto.getNote())
                .student(studentConverter.toEntity(examResultDto.getStudent()))
                .exam(examConverter.toEntity(examResultDto.getExam()))
                .clazz(classConverter.toEntity(examResultDto.getClazz()))
                .build();
    }
}
