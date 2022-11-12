package com.example.reactiveservice.controllers;

import com.example.reactiveservice.boundaries.ProductBoundary;
import com.example.reactiveservice.logic.db.exceptions.BadParameterException;
import com.example.reactiveservice.logic.db.ProductsService;
import com.example.reactiveservice.logic.db.SortOrder;
import com.example.reactiveservice.logic.db.filters.CategoryFilter;
import com.example.reactiveservice.logic.db.filters.NameFilter;
import com.example.reactiveservice.logic.db.filters.NoFilter;
import com.example.reactiveservice.logic.db.filters.PriceFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CatalogController {
    private final ProductsService productsService;

    @Autowired
    public CatalogController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping(path = "/catalog",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProductBoundary> create(@RequestBody ProductBoundary product) {
        return this.productsService.create(product);
    }

    @GetMapping(path = "/catalog/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProductBoundary> getById(@PathVariable String productId) {
        return this.productsService.getById(productId);
    }

    @GetMapping(path = "/catalog",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "This is for getting products based on category",
            parameters = @Parameter(name = "filterType", in = ParameterIn.QUERY, description = "The type to filter by",
                    examples = {
                            @ExampleObject(name = "byName", description = "Search for all product with the specified name (case-sensitive)."),
                            @ExampleObject(name = "byCategoryName", description = "Search for all products in the specified category (case-sensitive)."),
                            @ExampleObject(name = "byPrice", description = "Search for all products in the specified price range (exclusive).")
                    }
            )
    )
    public Flux<ProductBoundary> getAll(@RequestParam(required = false, defaultValue = "10") int size,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "ASC") SortOrder order,
                                        @RequestParam(required = false, defaultValue = "productId") String sortAttr) {
        return this.productsService.getAllByFilter(page, size, order, sortAttr, new NoFilter());
    }

    @GetMapping(path = "/catalog",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE,
            params = "filterType=byName"
    )
    public Flux<ProductBoundary> getAllByName(@RequestParam(required = false, name = "filterValue") String productName,
                                              @RequestParam(required = false, defaultValue = "10") int size,
                                              @RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "ASC") SortOrder order,
                                              @RequestParam(required = false, defaultValue = "productId") String sortAttr) {
        return this.productsService.getAllByFilter(page, size, order, sortAttr, new NameFilter(productName));
    }

    @GetMapping(path = "/catalog",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE,
            params = "filterType=byCategoryName"
    )
    public Flux<ProductBoundary> getAllByCategoryName(@RequestParam(required = false, name = "filterValue") String categoryName,
                                                      @RequestParam(required = false, defaultValue = "10") int size,
                                                      @RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "ASC") SortOrder order,
                                                      @RequestParam(required = false, defaultValue = "productId") String sortAttr) {
        return this.productsService.getAllByFilter(page, size, order, sortAttr, new CategoryFilter(categoryName));
    }

    @GetMapping(path = "/catalog",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE,
            params = "filterType=byPrice"
    )
    public Flux<ProductBoundary> getAllByPrice(@RequestParam(required = false) float minPrice, @RequestParam(required = false) float maxPrice,
                                               @RequestParam(required = false, defaultValue = "10") int size,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "ASC") SortOrder order,
                                               @RequestParam(required = false, defaultValue = "productId") String sortAttr) {
        return this.productsService.getAllByFilter(page, size, order, sortAttr, new PriceFilter(minPrice, maxPrice));
    }

    @GetMapping(path = "/catalog",
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "filterType"
    )
    public Flux<ProductBoundary> unknownFilter(@RequestParam(required = false) String filterType) {
        return Flux.error(() -> new BadParameterException("filterType", filterType));
    }


    @DeleteMapping(path = "/catalog")
    public Mono<Void> deleteAll() {
        return this.productsService.deleteAll();
    }
}