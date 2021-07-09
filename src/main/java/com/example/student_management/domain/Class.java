package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "classes")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classes_id_seq")
    @SequenceGenerator(name = "classes_id_seq", sequenceName = "classes_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 250)
    private String name;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "status", length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "clazz")
    private List<Event> events;

    @OneToMany(mappedBy = "clazz")
    private List<Registration> registrations;
}
