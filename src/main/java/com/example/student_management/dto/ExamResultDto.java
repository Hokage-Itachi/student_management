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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamResultDto {
    private Long id;
    private Integer score;
    private Date resultDate;
    private String note;
    private String student;
    private String exam;
    @JsonProperty("class")
    private String clazz;

}
