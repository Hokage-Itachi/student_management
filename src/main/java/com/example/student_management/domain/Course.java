package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courses_id_seq")
    @SequenceGenerator(name = "courses_id_seq", sequenceName = "courses_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @OneToMany(mappedBy = "course")
    private List<Class> classes;

    @OneToMany(mappedBy = "course")
    private List<Exam> exams;

    @OneToMany(mappedBy = "course")
    private List<Plan> plans;
}
