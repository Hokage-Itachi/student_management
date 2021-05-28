package com.example.student_management.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "descriptions", length = 100)
    private String descriptions;

    @ManyToMany
    @JoinTable(name = "authorization_each_role",
            joinColumns = @JoinColumn(name = "role_name"),
            inverseJoinColumns = @JoinColumn(name = "per_id"))
    private List<Permistion> permistions;

}
