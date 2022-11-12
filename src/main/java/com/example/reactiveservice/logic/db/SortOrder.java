package com.example.reactiveservice.logic.db;

import org.springframework.data.domain.Sort;

public enum SortOrder {
    ASC(Sort.Direction.ASC),
    DESC(Sort.Direction.DESC);

    private final Sort.Direction direction;

    SortOrder(Sort.Direction direction) {
        this.direction = direction;
    }

    public Sort.Direction getDirection() {
        return direction;
    }
}