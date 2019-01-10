package com.smort.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank(message = "{validation.product.name.blank}")
    private String name;

    @Min(0)
    private Double price;
    @ManyToOne
    private Vendor vendor;
    @ManyToOne
    private Category category;

}
