package com.smort.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private OrderStatus state;

    @ManyToOne
    private Customer customer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
        updated = LocalDateTime.now();
        state = OrderStatus.CREATED;
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }

    public Order addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        items.add(orderItem);
        return this;
    }

}
