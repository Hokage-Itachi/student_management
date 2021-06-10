package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registration {
    @EmbeddedId
    private RegistrationId id;

    @Column(name = "register_day")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDay;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    private Class clazz;
}
