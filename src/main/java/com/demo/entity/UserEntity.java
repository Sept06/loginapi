package com.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
public class UserEntity extends BaseEntity {

    @Column(name = "email")
    private String email;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "birthday")
    private Date birthday;

    private Integer gender;

    private String userCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<RoleEntity> roleEntities;


}
