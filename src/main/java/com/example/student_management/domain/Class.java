package com.example.student_management.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "classes")
@Getter
public class Class {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 250)
    private String name;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "status", length = 20)
    private String status;

    
}
