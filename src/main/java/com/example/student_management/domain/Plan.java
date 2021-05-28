package com.example.student_management.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "plans")
@Getter
@NoArgsConstructor
@Builder
public class Plan {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
