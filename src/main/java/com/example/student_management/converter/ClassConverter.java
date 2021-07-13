package com.example.student_management.converter;

import com.example.student_management.domain.Class;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.message.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClassConverter {

    private final CourseConverter courseConverter;
    private final TeacherConverter teacherConverter;

    public ClassConverter(CourseConverter courseConverter, TeacherConverter teacherConverter) {
        this.courseConverter = courseConverter;
        this.teacherConverter = teacherConverter;
    }

    public ClassDto toDto(Class entity) {
        if(entity == null){
            return null;
        }
        return ClassDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .course(courseConverter.toDto(entity.getCourse()))
                .teacher(teacherConverter.toDto(entity.getTeacher()))
                .build();
    }

    public Class toEntity(ClassDto classDto) {
        if (classDto == null) {
            return null;
        }
        return Class.builder()
                .id(classDto.getId())
                .name(classDto.getName())
                .startDate(classDto.getStartDate())
                .endDate(classDto.getEndDate())
                .status(classDto.getStatus())
                .course(courseConverter.toEntity(classDto.getCourse()))
                .teacher(teacherConverter.toEntity(classDto.getTeacher()))
                .build();
    }
}
