package com.example.reactiveservice.logic.db.filters;

import com.example.reactiveservice.data.ProductEntity;
import com.example.reactiveservice.logic.db.dao.ProductsDao;
import com.example.reactiveservice.logic.db.exceptions.BadParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public class CategoryFilter implements Filter {
    private final String category;

    public CategoryFilter(String category) {
        this.category = category;
    }

    @Override
    public Flux<ProductEntity> getResults(ProductsDao productsDao, Pageable pageable) {
        if (StringUtils.isBlank(this.category)) {
            return Flux.error(() -> new BadParameterException("category", this.category));
        }
        return productsDao.findAllByCategoryEquals(this.category, pageable);
    }
}