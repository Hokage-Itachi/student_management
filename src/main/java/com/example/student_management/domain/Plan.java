package com.example.student_management.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "plans")
@Getter
public class Plan {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 100)
    private String name;
}
