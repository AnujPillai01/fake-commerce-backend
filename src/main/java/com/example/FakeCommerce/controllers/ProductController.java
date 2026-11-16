package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.ApiResponseDto;
import com.example.FakeCommerce.dtos.CreateProductRequestDto;
import com.example.FakeCommerce.dtos.GetProductResponseDto;
import com.example.FakeCommerce.dtos.GetProductWithDetailsResponseDto;
import com.example.FakeCommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<GetProductResponseDto>>> getAllProducts() {
        List<GetProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponseDto.success("Products fetched successfully", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetProductResponseDto>> getProductById(@PathVariable Long id) {
        GetProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponseDto.success("Product fetched successfully", product));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponseDto<GetProductWithDetailsResponseDto>> getProductWithDetails(@PathVariable Long id){
        GetProductWithDetailsResponseDto productWithDetails = productService.getProductDetails(id);
        return ResponseEntity.ok(ApiResponseDto.success("Product fetched successfully with details", productWithDetails));
    }

    @PostMapping("/vendor/{vendorId}")
    public ResponseEntity<ApiResponseDto<GetProductResponseDto>> createProduct(
            @PathVariable Long vendorId,
            @RequestBody CreateProductRequestDto requestDto) {
        GetProductResponseDto product = productService.createProduct(vendorId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Product created successfully", product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponseDto.success("Product deleted successfully", null));
    }

//   I used path variables for direct sub-resource browsing, but if we need
//   multi-criteria filtering in the future, transitioning to a single /api/v1/products
//   endpoint with optional query parameters (@RequestParam) would scale better.

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponseDto<List<GetProductResponseDto>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<GetProductResponseDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponseDto.success("Category products fetched successfully", products));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<ApiResponseDto<List<GetProductResponseDto>>> getProductsByVendor(@PathVariable Long vendorId) {
        List<GetProductResponseDto> products = productService.getProductsByVendor(vendorId);
        return ResponseEntity.ok(ApiResponseDto.success("Vendor products fetched successfully", products));
    }
}
