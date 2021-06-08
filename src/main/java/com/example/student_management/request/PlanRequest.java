package com.example.student_management.request;

import com.example.student_management.dto.PlanDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequest {
    private PlanDto plan;
    private Long courseId;
}
