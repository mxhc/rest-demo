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

    @Column(unique=true)
    private String userName;

    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    private String firstName;

    private String lastName;

    private String country;

    private boolean enabled;

    @Column(unique=true)
    private String email;

    public UserInfo addRole(Role role) {
        role.setUser(this);
        roles.add(role);
        return this;
    }
}
