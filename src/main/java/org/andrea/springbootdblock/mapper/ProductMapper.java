package org.andrea.springbootdblock.mapper;

import org.andrea.springbootdblock.dto.request.ProductRequest;
import org.andrea.springbootdblock.dto.response.ProductResponse;
import org.andrea.springbootdblock.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product productRequestToProduct(ProductRequest productRequest);

    ProductResponse productToProductResponse(Product product);
}
