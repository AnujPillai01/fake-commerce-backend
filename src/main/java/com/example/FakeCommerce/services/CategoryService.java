package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.CategoryAdapter;
import com.example.FakeCommerce.dtos.CreateCategoryRequestDto;
import com.example.FakeCommerce.dtos.GetCategoryResponseDto;
import com.example.FakeCommerce.repositories.CategoryRepository;
import com.example.FakeCommerce.schema.Category;
import com.example.FakeCommerce.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository  categoryRepository;
    private final CategoryAdapter categoryAdapter;

    @Transactional
    public GetCategoryResponseDto createCategory(CreateCategoryRequestDto requestDto){
        String formattedName = StringUtils.toTitleCase(requestDto.getName());

        if(categoryRepository.existsByNameIgnoreCase(formattedName)){
            throw new RuntimeException("Category already exists with name: " + formattedName);
        }
        Category newCategory = Category.builder()
                .name(formattedName)
                .build();

        categoryRepository.save(newCategory);

        return categoryAdapter.mapToGetCategoryResponseDto(newCategory);
    }

    public List<GetCategoryResponseDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categoryAdapter.mapToGetCategoryResponseDtoList(categories);
    }

    public GetCategoryResponseDto getCategoryById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Category not found"));

        return categoryAdapter.mapToGetCategoryResponseDto(category);
    }

    @Transactional
    public void  deleteCategoryById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}
