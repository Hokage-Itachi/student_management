package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teachers_id_seq")
    @SequenceGenerator(name = "teachers_id_seq", sequenceName = "teachers_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "full_name", length = 250, nullable = false)
    private String fullName;

    @Column(name = "email", length = 100, unique = true)
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
