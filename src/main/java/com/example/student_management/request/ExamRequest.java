package com.example.student_management.request;

import com.example.student_management.dto.ExamDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {
    private ExamDto exam;
    private Long courseId;
}
