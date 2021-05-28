package com.example.student_management.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exam_results")
@Getter
public class ExamResult {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "score")
    private Integer score;

    @Column(name = "result_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resultDate;

    @Column(name = "note", length = 2000)
    private String note;


}
