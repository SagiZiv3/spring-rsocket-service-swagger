package com.example.reactiveservice.logic.db.filters;

import com.example.reactiveservice.data.ProductEntity;
import com.example.reactiveservice.logic.db.dao.ProductsDao;
import com.example.reactiveservice.logic.db.exceptions.BadParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public class NameFilter implements Filter{
    private final String name;

    public NameFilter(String name) {
        this.name = name;
    }

    @Override
    public Flux<ProductEntity> getResults(ProductsDao productsDao, Pageable pageable) {
        if (StringUtils.isBlank(this.name)){
            return Flux.error(()->new BadParameterException("name", this.name));
        }
        return productsDao.findAllByNameEquals(this.name, pageable);
    }
}