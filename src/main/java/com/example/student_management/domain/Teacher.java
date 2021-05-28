package com.example.student_management.domain;

import lombok.Getter;
import javax.persistence.*;

@Entity
@Table(name = "teachers")
@Getter
public class Teacher {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "full_name", length = 250)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "address", length = 250)
    private String address;

    @Column(name = "grade", length = 20)
    private String grade;

}
