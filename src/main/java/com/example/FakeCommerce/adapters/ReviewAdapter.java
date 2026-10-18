package com.example.FakeCommerce.adapters;

import com.example.FakeCommerce.dtos.GetReviewResponseDto;
import com.example.FakeCommerce.schema.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewAdapter {

    public List<GetReviewResponseDto> maptoGetReviewResponseDtoList(List<Review> reviews){
        return reviews.stream().map(this::maptoGetReviewResponseDto).toList();
    }

    public GetReviewResponseDto maptoGetReviewResponseDto(Review review){
        return GetReviewResponseDto
                .builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .orderId(review.getOrder().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }
}
