package com.example.student_management.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "exams")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exams_id_seq")
    @SequenceGenerator(name = "exams_id_seq", sequenceName = "exams_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 500)
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "exam")
    private List<ExamResult> examResults;

}
