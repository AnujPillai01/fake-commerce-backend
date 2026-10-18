package com.example.FakeCommerce.services;

import com.example.FakeCommerce.dtos.CreateProductRequestDto;
import com.example.FakeCommerce.dtos.GetProductResponseDto;
import com.example.FakeCommerce.dtos.GetProductWithDetailsResponseDto;
import com.example.FakeCommerce.repositories.CategoryRepository;
import com.example.FakeCommerce.repositories.ProductRepository;
import com.example.FakeCommerce.schema.Category;
import com.example.FakeCommerce.schema.Product;
import com.example.FakeCommerce.services.cache.ProductRedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryService categoryService;
  private final ProductRedisCache productRedisCache;

  public List<GetProductResponseDto> getAllProducts() {
      return productRepository.findAll().stream()
              .map(product -> GetProductResponseDto.builder()
                      .id(product.getId())
                      .description(product.getDescription())
                      .title(product.getTitle())
                      .rating(product.getRating())
                      .image(product.getImage())
                      .price(product.getPrice())
                      .build())
              .collect(Collectors.toList());
  }

    public GetProductResponseDto getProductById(Long id) {
        Optional<GetProductResponseDto> cachedSummary = productRedisCache.getSummary(id);

        if(cachedSummary.isPresent()) {
            return cachedSummary.get();
        }

        GetProductResponseDto responseDto = productRepository.findById(id)
                    .map(product -> GetProductResponseDto.builder()
                            .id(product.getId())
                            .description(product.getDescription())
                            .title(product.getTitle())
                            .rating(product.getRating())
                            .image(product.getImage())
                            .price(product.getPrice())
                            .build())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

        productRedisCache.putSummary(id,responseDto);
        return responseDto;
    }

  public GetProductWithDetailsResponseDto getProductWithDetails(Long id) {
      Product product =  productRepository.findProductWithDetails(id).get(0);
      return GetProductWithDetailsResponseDto.builder()
                      .id(product.getId())
                      .description(product.getDescription())
                      .title(product.getTitle())
                       .category(product.getCategory().getName())
                      .rating(product.getRating())
                      .image(product.getImage())
                      .price(product.getPrice())
                      .build();
  }

  public Product createProduct(CreateProductRequestDto requestDto) {
      Category category = categoryService.getCategoryById(requestDto.getCategoryId());
      Product newProduct = Product.builder()
              .title(requestDto.getTitle())
              .description(requestDto.getDescription())
              .price(requestDto.getPrice())
              .category(category)
              .rating(requestDto.getRating())
              .image(requestDto.getImage())
              .build();

      return productRepository.save(newProduct);
  }

  public void deleteProduct(Long id) {
      productRepository.deleteById(id);
  }

  public List<Product> getProductsByCategory(String category){
      return productRepository.findByCategory(category);
  }

  public List<String> getUniqueCategories(){
      return productRepository.findAllCategories();
  }
}
