package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "permistions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permistion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permistions_id_seq")
    @SequenceGenerator(name = "permistions_id_seq", sequenceName = "permistions_id_seq", allocationSize = 1)
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
