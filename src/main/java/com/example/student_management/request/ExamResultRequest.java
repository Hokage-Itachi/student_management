package com.example.student_management.request;

import com.example.student_management.dto.ExamResultDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultRequest {
    private ExamResultDto examResult;
    private Long studentId;
    private Long classId;
    private Long examId;
}
