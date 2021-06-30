package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "students_id_seq")
    @SequenceGenerator(name = "students_id_seq", sequenceName = "students_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "full_name", length = 250, nullable = false)
    private String fullName;

    @Column(name = "address", length = 250, nullable = false)
    private String address;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "note", length = 2000)
    private String note;

    @Column(name = "facebook", length = 100)
    private String facebook;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @OneToMany(mappedBy = "student")
    private List<ExamResult> examResults;

    @OneToMany(mappedBy = "student")
    private List<Registration> registrations;

}
