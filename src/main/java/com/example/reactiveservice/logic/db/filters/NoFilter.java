package com.example.reactiveservice.logic.db.filters;

import com.example.reactiveservice.data.ProductEntity;
import com.example.reactiveservice.logic.db.dao.ProductsDao;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public class NoFilter implements Filter{
    @Override
    public Flux<ProductEntity> getResults(ProductsDao productsDao, Pageable pageable) {
        return productsDao.findAllByProductIdNotNull(pageable);
    }
}