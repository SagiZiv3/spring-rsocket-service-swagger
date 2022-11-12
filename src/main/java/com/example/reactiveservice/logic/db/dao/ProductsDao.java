package com.example.reactiveservice.logic.db.dao;

import com.example.reactiveservice.data.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductsDao extends ReactiveMongoRepository<ProductEntity, String> {
    Flux<ProductEntity> findAllByProductIdNotNull(Pageable pageable);

    Flux<ProductEntity> findAllByNameEquals(String name, Pageable pageable);

    Flux<ProductEntity> findAllByCategoryEquals(String name, Pageable pageable);

    Flux<ProductEntity> findAllByPriceBetween(float minPrice, float maxPrice, Pageable pageable);
}