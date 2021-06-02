package com.example.student_management.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "plans")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "plans_id_seq")
    @SequenceGenerator(name = "plans_id_seq", sequenceName = "plans_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
