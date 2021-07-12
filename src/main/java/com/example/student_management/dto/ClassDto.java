package com.example.student_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassDto {
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String status;
    private String course;
    private String teacher;
    private List<EventDto> events;
    private List<String> students;
}
