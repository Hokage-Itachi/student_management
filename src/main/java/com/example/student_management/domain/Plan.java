package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "plans_id_seq")
    @SequenceGenerator(name = "plans_id_seq", sequenceName = "plans_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
