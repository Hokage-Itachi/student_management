package com.example.student_management.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "full_name", length = 250, nullable = false)
    private String fullName;

    @Column(name = "address", length = 250, nullable = false)
    private String address;

    @Column(name = "email", length = 100, nullable = false)
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


    @ManyToMany(mappedBy = "students")
    private List<Class> classes;

}
