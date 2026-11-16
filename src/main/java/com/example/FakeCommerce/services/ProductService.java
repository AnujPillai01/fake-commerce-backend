package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.ProductAdapter;
import com.example.FakeCommerce.dtos.CreateProductRequestDto;
import com.example.FakeCommerce.dtos.GetProductResponseDto;
import com.example.FakeCommerce.dtos.GetProductWithDetailsResponseDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.ProductRepository;
import com.example.FakeCommerce.schema.Category;
import com.example.FakeCommerce.schema.Product;
import com.example.FakeCommerce.schema.Vendor;
import com.example.FakeCommerce.schema.VendorStatus;
import com.example.FakeCommerce.services.cache.ProductRedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductAdapter productAdapter;
    private final ProductRedisCache productRedisCache;
    private final VendorService vendorService;

    public List<GetProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productAdapter.mapToGetProductResponseDtoList(products);
    }

    public GetProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        return productAdapter.mapToGetProductResponseDto(product);
    }

    @Transactional
    public GetProductResponseDto createProduct(Long vendorId, CreateProductRequestDto requestDto) {
        Vendor vendor = vendorService.getVendorByProfile(vendorId);
        if (vendor.getStatus() != VendorStatus.APPROVED) {
            throw new RuntimeException("Vendor account is not approved to list products!");
        }

        Category category = categoryService.getCategoryById(requestDto.getCategoryId());

        // Generate a unique SKU for database constraint compliance
        String generatedSku = "SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Product newProduct = Product.builder()
                .title(requestDto.getTitle())
                .sku(generatedSku) // FIXED: Populated required non-null SKU
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .category(category)
                .vendor(vendor)
                .image(requestDto.getImage())
                .stockQuantity(requestDto.getQuantity())
                .build();

        productRepository.save(newProduct);

        return productAdapter.mapToGetProductResponseDto(newProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        productRepository.delete(product); // Triggers soft delete via @SQLDelete
    }

    public List<GetProductResponseDto> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return productAdapter.mapToGetProductResponseDtoList(products);
    }

    public List<GetProductResponseDto> getProductsByVendor(Long vendorId) {
        List<Product> products = productRepository.findByVendorId(vendorId);
        return productAdapter.mapToGetProductResponseDtoList(products);
    }

    @Transactional(readOnly = true)
    public GetProductWithDetailsResponseDto getProductDetails(Long id) {
        Optional<GetProductWithDetailsResponseDto> cachedSummary = productRedisCache.getSummary(id);
        if (cachedSummary.isPresent()) {
            return cachedSummary.get(); // Cache hit! Returns instantly without touching MySQL
        }

        Product product = productRepository.findProductWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        GetProductWithDetailsResponseDto responseDto = productAdapter.mapToGetProductWithDetailsResponseDto(product);

        productRedisCache.putSummary(id, responseDto);
        return responseDto;
    }
}