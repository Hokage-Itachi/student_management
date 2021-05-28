package com.example.student_management.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "permistions")
@Getter
public class Permistion {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "per_name", length = 200)
    private String perName;


}
