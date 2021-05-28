package com.example.student_management.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "exams")
@Getter
public class Exam {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 500)
    private String name;
}
