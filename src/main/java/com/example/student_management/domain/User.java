package com.example.student_management.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
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


}
