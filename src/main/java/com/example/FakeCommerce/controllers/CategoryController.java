package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.ApiResponseDto;
import com.example.FakeCommerce.dtos.CreateCategoryRequestDto;
import com.example.FakeCommerce.dtos.GetCategoryResponseDto;
import com.example.FakeCommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<GetCategoryResponseDto>>> getAllCategories(){
        List<GetCategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponseDto.success("Categories fetched successfully", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetCategoryResponseDto>> getCategoryById(@PathVariable Long id){
        GetCategoryResponseDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponseDto.success("Category fetched successfully", category));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<GetCategoryResponseDto>> createCategory(@RequestBody CreateCategoryRequestDto requestDto){
        GetCategoryResponseDto category = categoryService.createCategory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Category created successfully", category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(ApiResponseDto.success("Category deleted successfully", null));
    }
}