package com.smort.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String country;

    private Boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<Order> orders;

    @Column(unique=true, nullable = false)
    private String email;

    public UserInfo addRole(Role role) {
        role.setUser(this);
        roles.add(role);
        return this;
    }
}
