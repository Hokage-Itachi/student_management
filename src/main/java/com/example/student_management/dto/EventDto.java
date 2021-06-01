package com.example.student_management.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private String name;
    private Date createDate;
    private String status;
    private Date happenDate;
    private ClassDto clazz;

}
