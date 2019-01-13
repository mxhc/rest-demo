package com.smort.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    CREATED, ORDERED, RECEIVED, CANCELED

}
