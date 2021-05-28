package com.example.student_management.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "events")
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 250)
    private String name;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "happen_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date happenDate;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class clazz;

}
