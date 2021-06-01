package com.example.student_management.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "teacher")
    private List<Class> classes;
}
