package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exam_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_results_id_seq")
    @SequenceGenerator(name = "exam_results_id_seq", sequenceName = "exam_results_id_seq", allocationSize = 1)
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

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class clazz;

}
