package com.example.reactiveservice.logic.db;

import com.example.reactiveservice.boundaries.ProductBoundary;
import com.example.reactiveservice.logic.db.filters.Filter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductsService {
    Mono<ProductBoundary> create(ProductBoundary productBoundary);
    Mono<ProductBoundary> getById(String productId);
    Flux<ProductBoundary> getAllByFilter(int page, int size, SortOrder order, String sortAttr, Filter filter);
    Mono<Void> deleteAll();
}