package com.smort.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{custome.first.name.blank}")
    @Size(min = 2, message = "{customer.first.name.minsize}")
    private String firstname;

    @NotBlank(message = "{custome.last.name.blank}")
    @Size(min = 2, message = "{customer.last.name.minsize}")
    private String lastname;
}
