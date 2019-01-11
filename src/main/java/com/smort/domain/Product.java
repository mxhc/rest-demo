package com.smort.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{product.name.blank}")
    @Size(min = 2, message = "{product.name.minsize}")
    private String name;

    @PositiveOrZero(message = "{product.price.negative}")
    @ApiModelProperty(value = "Product Price", required = true, example = "510.32", position = 1)
    private Double price;

    @NotNull(message = "{product.vendor}")
    @ManyToOne
    private Vendor vendor;

    @NotNull(message = "{product.category}")
    @ManyToOne
    private Category category;

}
