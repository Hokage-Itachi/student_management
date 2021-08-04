package com.example.student_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResultDto {
    private Long id;
    private Integer score;
    private Date resultDate;
    private String note;
    private StudentDto student;
    private ExamDto exam;
    @JsonProperty("class")
    private ClassDto clazz;

}
