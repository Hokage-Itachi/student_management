package com.example.student_management.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "exams")
@Getter
@NoArgsConstructor
@Builder
public class Exam {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 500)
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "exam")
    private List<ExamResult> examResults;

}
