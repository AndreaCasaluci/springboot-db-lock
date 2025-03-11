package org.andrea.springbootdblock.controller;

import org.andrea.springbootdblock.dto.request.ProductRequest;
import org.andrea.springbootdblock.dto.response.ProductResponse;
import org.andrea.springbootdblock.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponse create(@RequestBody ProductRequest product) {
        return productService.createProduct(product);
    }

    @PatchMapping("{guid}/optimistic")
    public ProductResponse updateOptimistic(@PathVariable UUID guid, @RequestParam int quantity) {
        return productService.updateProductOptimistic(guid, quantity);
    }

    @PatchMapping("/{guid}/pessimistic")
    public ProductResponse updatePessimistic(@PathVariable UUID guid, @RequestParam int quantity) {
        return productService.updateProductPessimistic(guid, quantity);
    }
}
