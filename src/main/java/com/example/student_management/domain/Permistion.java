package com.example.student_management.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "permistions")
@Getter
@NoArgsConstructor
@Builder
public class Permistion {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "per_name", length = 200)
    private String perName;

    @ManyToMany(mappedBy = "permistions")
    private List<User> users;

    @ManyToMany(mappedBy = "permistions")
    private List<Role> roles;

}
