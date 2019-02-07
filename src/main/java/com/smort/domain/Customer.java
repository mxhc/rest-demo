package com.smort.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{custome.first.name.blank}")
    @Size(min = 2, message = "{customer.first.name.minsize}")
    private String firstName;

    @NotBlank(message = "{custome.last.name.blank}")
    @Size(min = 2, message = "{customer.last.name.minsize}")
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "customer")
    private List<Order> orders;
}
