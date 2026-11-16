package com.example.FakeCommerce.adapters;

import com.example.FakeCommerce.dtos.GetProductResponseDto;
import com.example.FakeCommerce.dtos.GetProductWithDetailsResponseDto;
import com.example.FakeCommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductAdapter {

    public GetProductResponseDto mapToGetProductResponseDto(Product product) {
        if (product == null) {
            return null;
        }

        return GetProductResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .stockQuantity(product.getStockQuantity())
                .build();
    }

    public List<GetProductResponseDto> mapToGetProductResponseDtoList(List<Product> products) {
        if (products == null) {
            return List.of();
        }

        return products.stream()
                .map(this::mapToGetProductResponseDto)
                .collect(Collectors.toList());
    }

    public GetProductWithDetailsResponseDto mapToGetProductWithDetailsResponseDto(Product product) {
        if (product == null) {
            return null;
        }

        return GetProductWithDetailsResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory() != null ? product.getCategory().getName() : null)
                .vendor(product.getVendor() != null ? product.getVendor().getStoreName() : null) // or vendor shop name if you have one
                .build();
    }

    public List<GetProductWithDetailsResponseDto> mapToGetProductWithDetailsResponseDtoList(List<Product> products) {
        if (products == null) {
            return List.of();
        }

        return products.stream()
                .map(this::mapToGetProductWithDetailsResponseDto)
                .collect(Collectors.toList());
    }
}