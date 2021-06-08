package com.example.student_management.request;

import com.example.student_management.dto.ClassDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRequest {
    @JsonProperty("class")
    private ClassDto clazz;
    private Long teacherId;
    private Long courseId;
}
