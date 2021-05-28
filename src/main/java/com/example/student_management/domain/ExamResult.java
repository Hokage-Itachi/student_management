package com.example.student_management.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exam_results")
@Getter
@NoArgsConstructor
@Builder
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

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

}
