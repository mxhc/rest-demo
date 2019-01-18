package com.smort.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
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

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "users", fetch = FetchType.EAGER)
    private List<Role> roles;

    private String firstName;

    private String lastName;

    private String country;

    private short enabled;

    private String email;

}
