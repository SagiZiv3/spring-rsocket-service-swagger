package com.example.reactiveservice.logic.db;

import com.example.reactiveservice.boundaries.ProductBoundary;
import com.example.reactiveservice.logic.db.exceptions.BadParameterException;
import com.example.reactiveservice.data.ProductEntity;
import com.example.reactiveservice.logic.db.dao.ProductsDao;
import com.example.reactiveservice.logic.db.filters.Filter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductsServiceInMongoImpl implements ProductsService {
    private final ProductsDao productsDao;
    private final Set<String> entityFields;

    @Autowired
    public ProductsServiceInMongoImpl(ProductsDao productsDao) {
        this.productsDao = productsDao;
        // Source: https://www.tutorialspoint.com/get-the-list-of-all-declared-fields-in-java#:~:text=The%20list%20of%20all%20declared%20fields%20can%20be%20obtained%20using,an%20array%20of%20field%20objects.
        // We get all the fields in the product entity, so we can validate the orderBy attribute.
        this.entityFields = Arrays.stream(ProductEntity.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public Mono<ProductBoundary> create(ProductBoundary productBoundary) {
        return Mono.just(productBoundary)
                .flatMap(boundary -> {
                    if (!validateBoundary(boundary)) {
                        return Mono.error(() ->
                                new BadParameterException("Invalid product! Make sure id is not null and none of the fields is blank.", "product", boundary));
                    }
                    return Mono.just(boundary);
                })
                .map(this::toEntity)
                .flatMap(entity ->
                        this.productsDao.existsById(entity.getProductId()) // Mono<Boolean>
                                .flatMap(exists -> exists ?
                                        Mono.error(() -> new RuntimeException("Entity already exists!")) :
                                        this.productsDao.save(entity))
                )
                .map(this::toBoundary)
                .log();
    }

    @Override
    public Mono<ProductBoundary> getById(String productId) {
        return this.productsDao
                .findById(productId)
                .map(this::toBoundary)
                .log();
    }

    @Override
    public Flux<ProductBoundary> getAllByFilter(int page, int size, SortOrder order, String sortAttr, Filter filter) {
        if (!this.entityFields.contains(sortAttr)) {
            return Flux.error(() ->
                    new BadParameterException("Unknown attribute: '" + sortAttr + "'. Options: " + this.entityFields, "sortAttr", sortAttr));
        }
        PageRequest pageable = PageRequest.of(page, size, order.getDirection(), sortAttr);
        return filter.getResults(this.productsDao, pageable)
                .map(this::toBoundary)
                .log();
    }

    @Override
    public Mono<Void> deleteAll() {
        return this.productsDao.deleteAll();
    }

    public ProductBoundary toBoundary(ProductEntity entity) {
        ProductBoundary rv = new ProductBoundary();
        rv.setProductId(entity.getProductId());
        rv.setCategory(entity.getCategory());
        rv.setName(entity.getName());
        rv.setDescription(entity.getDescription());
        rv.setPrice(entity.getPrice());
        rv.setProductDetails(entity.getProductDetails());

        return rv;
    }

    public ProductEntity toEntity(ProductBoundary boundary) {
        ProductEntity rv = new ProductEntity();
        rv.setProductId(boundary.getProductId());
        rv.setCategory(boundary.getCategory());
        rv.setName(boundary.getName());
        rv.setDescription(boundary.getDescription());
        rv.setPrice(boundary.getPrice());
        rv.setProductDetails(boundary.getProductDetails());

        return rv;
    }

    private boolean validateBoundary(ProductBoundary boundary) {
        return boundary.getProductId() != null && StringUtils.isNotBlank(boundary.getName()) &&
                StringUtils.isNotBlank(boundary.getCategory()) && boundary.getPrice() >= 0.0;
    }
}