package com.example.student_management.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements Serializable {
    @Id
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "descriptions", length = 100)
    private String descriptions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorization_each_role",
            joinColumns = @JoinColumn(name = "role_name"),
            inverseJoinColumns = @JoinColumn(name = "per_id"))
    private Set<Permission> permissions;

    @OneToMany(mappedBy = "role")
    private Set<User> users;

}
