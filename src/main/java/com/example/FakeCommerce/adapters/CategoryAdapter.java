package com.example.FakeCommerce.adapters;

import com.example.FakeCommerce.dtos.GetCategoryResponseDto;
import com.example.FakeCommerce.schema.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryAdapter {

    public GetCategoryResponseDto mapToGetCategoryResponseDto(Category category) {
        if (category == null) {
            return null;
        }

        return GetCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<GetCategoryResponseDto> mapToGetCategoryResponseDtoList(List<Category> categories) {
        if (categories == null) {
            return List.of();
        }

        return categories.stream()
                .map(this::mapToGetCategoryResponseDto)
                .collect(Collectors.toList());
    }
}
