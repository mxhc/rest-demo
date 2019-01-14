package com.smort.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {

    CREATED("CREATED"), ORDERED("ORDERED"), RECEIVED("RECEIVED"), CANCELED("CANCELED");

    private String string;

    OrderStatus(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

}
