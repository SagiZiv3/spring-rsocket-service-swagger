package com.example.reactiveservice.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Document(collection = "products_catalog")
public class ProductEntity {
    @Id
    private String productId;
    private String name;
    private float price;
    private String description;
    private Map<String, Object> productDetails;
    private String category;
}