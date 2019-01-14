package com.smort.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    @NotNull(message = "{item.price.null}")
    @Positive(message = "{item.price.negative}")
    private Double price;

    @NotNull(message = "{item.quantity.null}")
    @Positive(message = "{item.quantity.negative}")
    private Integer quantity;

    public OrderItem(Product product, Double price, Integer quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem() {}

    public OrderItem(Product product, Order order, Double price, Integer quantity) {
        this.product = product;
        this.order = order;
        this.price = price;
        this.quantity = quantity;
    }

}
