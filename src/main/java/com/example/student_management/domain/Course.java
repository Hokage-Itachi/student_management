package com.example.student_management.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
public class Course {
    @Id
    @GeneratedValue
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
