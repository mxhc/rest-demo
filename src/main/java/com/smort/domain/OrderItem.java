package com.smort.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    private Double price;

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
