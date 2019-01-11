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
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{vendor.name.blank}")
    @Size(min = 2, message = "{vendor.name.minsize}")
    private String name;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    private List<Product> products;
}
