package com.example.student_management.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "descriptions", length = 100)
    private String descriptions;

}
