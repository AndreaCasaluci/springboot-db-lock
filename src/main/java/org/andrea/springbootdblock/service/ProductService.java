package org.andrea.springbootdblock.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.andrea.springbootdblock.dto.request.ProductRequest;
import org.andrea.springbootdblock.dto.response.ProductResponse;
import org.andrea.springbootdblock.entity.Product;
import org.andrea.springbootdblock.mapper.ProductMapper;
import org.andrea.springbootdblock.repository.ProductRepository;
import org.andrea.springbootdblock.repository.ProductRepositoryPessimisticLock;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRepositoryPessimisticLock productRepositoryPessimisticLock;

    public ProductService(ProductRepository productRepository, ProductRepositoryPessimisticLock  productRepositoryPessimisticLock) {
        this.productRepository = productRepository;
        this.productRepositoryPessimisticLock = productRepositoryPessimisticLock;
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = ProductMapper.INSTANCE.productRequestToProduct(productRequest);

        product = productRepository.save(product);

        return ProductMapper.INSTANCE.productToProductResponse(product);
    }

    @Transactional
    public ProductResponse updateProductOptimistic(UUID guid, int newQuantity) {
        Product product = productRepository.findByGuid(guid)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setQuantity(newQuantity);

        try {
            product = productRepository.save(product);
            return ProductMapper.INSTANCE.productToProductResponse(product);

        } catch (OptimisticLockException e) {
            throw new RuntimeException("Optimistic locking failure, please retry", e);
        }
    }

    @Transactional
    public ProductResponse updateProductPessimistic(UUID guid, int newQuantity) {
        Product product = productRepositoryPessimisticLock.findByGuid(guid)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setQuantity(newQuantity);

        product = productRepository.save(product);

        return ProductMapper.INSTANCE.productToProductResponse(product);
    }
}
