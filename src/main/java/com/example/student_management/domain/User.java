package com.example.student_management.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(name = "users_user_id_seq", sequenceName = "users_user_id_seq", allocationSize = 1)
    @NotNull
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 50)
    private String fullName;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "last_login_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    @Column(name = "lockout_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lockoutDate;

    @Column(name = "login_failed_count")
    private Integer loginFailedCount;

    @Column(name = "register_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Column(name = "forgot_password_token", length = 100)
    private String forgotPasswordToken;

    @OneToOne
    @JoinColumn(name = "role")
    private Role role;

    @ManyToMany
    @JoinTable(name = "authorization_each_author",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "per_id"))
    private List<Permistion> permistions;


}
