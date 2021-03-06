package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_id_seq")
    @SequenceGenerator(name = "events_id_seq", sequenceName = "events_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 250, nullable = false)
    private String name;

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "happen_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date happenDate;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class clazz;

}
