package com.example.reactiveservice.logic.db.filters;

import com.example.reactiveservice.data.ProductEntity;
import com.example.reactiveservice.logic.db.dao.ProductsDao;
import com.example.reactiveservice.logic.db.exceptions.BadParameterException;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public class PriceFilter implements Filter {
    private final float minPrice, maxPrice;

    public PriceFilter(float minPrice, float maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public Flux<ProductEntity> getResults(ProductsDao productsDao, Pageable pageable) {
        if (this.minPrice > this.maxPrice) {
            return Flux.error(() ->
                    new BadParameterException("Minimum price can't be higher than the maximum price!", "max price", this.maxPrice));
        }
        return productsDao.findAllByPriceBetween(this.minPrice, this.maxPrice, pageable);
    }
}