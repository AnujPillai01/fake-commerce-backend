package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.ApiResponseDto;
import com.example.FakeCommerce.dtos.CreateReviewRequestDto;
import com.example.FakeCommerce.dtos.GetReviewResponseDto;
import com.example.FakeCommerce.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<GetReviewResponseDto>>> getAllReviews(){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("Reviews found",reviewService.getAllReviews()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetReviewResponseDto>> getReviewById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("Review found",reviewService.getReviewById(id)));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponseDto<List<GetReviewResponseDto>>> getReviewsByProductId(@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("Reviews found",reviewService.getReviewsByProductId(productId)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponseDto<List<GetReviewResponseDto>>> getReviewsByOrderId(@PathVariable Long orderId){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("Reviews found",reviewService.getReviewsByOrderId(orderId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<GetReviewResponseDto>> createReview(@RequestBody CreateReviewRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success("Review created",reviewService.createReview(requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteReview(@PathVariable Long id){
        reviewService.deleteReviewById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("Review deleted",null));
    }
}
