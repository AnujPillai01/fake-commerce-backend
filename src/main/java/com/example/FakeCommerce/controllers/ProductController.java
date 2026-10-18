package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.CreateProductRequestDto;
import com.example.FakeCommerce.dtos.GetProductResponseDto;
import com.example.FakeCommerce.dtos.GetProductWithDetailsResponseDto;
import com.example.FakeCommerce.schema.Product;
import com.example.FakeCommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<GetProductResponseDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}/details")
    public GetProductWithDetailsResponseDto getProductWithDetails(@PathVariable Long id){
        return  productService.getProductWithDetails(id);
    }

    @GetMapping("/{id}")
    public GetProductResponseDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }



    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequestDto requestDto){
       return productService.createProduct(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id){
        productService.deleteProduct(id);
    }

    @GetMapping("/search")
    public List<Product> getProductsByCategory(@RequestParam("categoryName") String category, @RequestParam(required = false, name="myPage") Integer page){
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/category")
    public List<String> getUniqueCategories(){
        return productService.getUniqueCategories();
    }
}
