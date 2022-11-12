package com.example.reactiveservice.boundaries;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class ProductBoundary {
    private String productId;
    private String name;
    private float price;
    private String description;
    private Map<String, Object> productDetails;
    private String category;
}