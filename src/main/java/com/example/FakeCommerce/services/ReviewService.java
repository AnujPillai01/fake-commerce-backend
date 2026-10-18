package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.ReviewAdapter;
import com.example.FakeCommerce.dtos.CreateReviewRequestDto;
import com.example.FakeCommerce.dtos.GetReviewResponseDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.OrderItemRepository;
import com.example.FakeCommerce.repositories.OrderRepository;
import com.example.FakeCommerce.repositories.ProductRepository;
import com.example.FakeCommerce.repositories.ReviewRepository;
import com.example.FakeCommerce.schema.Order;
import com.example.FakeCommerce.schema.OrderItem;
import com.example.FakeCommerce.schema.Product;
import com.example.FakeCommerce.schema.Review;
import lombok.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewAdapter reviewAdapter;

    public List<GetReviewResponseDto> getAllReviews(){
        List<Review> reviews = reviewRepository.findAll();
        return reviewAdapter.maptoGetReviewResponseDtoList(reviews);
    }

    public GetReviewResponseDto getReviewById(long id){
        Review review = reviewRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Review not found"));

        return reviewAdapter.maptoGetReviewResponseDto(review);
    }

    public List<GetReviewResponseDto> getReviewsByProductId(long id){
        List<Review> reviews = reviewRepository.findAllByProductId(id);
        return reviewAdapter.maptoGetReviewResponseDtoList(reviews);
    }

    public List<GetReviewResponseDto> getReviewsByOrderId(long id){
        List<Review> reviews = reviewRepository.findAllByOrderId(id);
        return reviewAdapter.maptoGetReviewResponseDtoList(reviews);
    }

    public GetReviewResponseDto createReview(CreateReviewRequestDto requestDto){
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + requestDto.getOrderId()));

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + requestDto.getProductId()));

        List<OrderItem> items = orderItemRepository.findByOrderWithProduct(order);
        boolean productExistsInOrder = false;
        for(var item:items){
            if (item.getProduct().getId().equals(requestDto.getProductId())){
                log.info("product exists in the order");
                productExistsInOrder = true;
                break;
            }
        }

        if(!productExistsInOrder){
            throw new RuntimeException("Cannot review a product that was not part of this order");
        }

        boolean exists = reviewRepository.existsByOrderIdAndProductId(requestDto.getOrderId(), requestDto.getProductId());
        if(exists){
            throw new RuntimeException("You have already reviewed this product for this order");
        }

        Review review = Review
                .builder()
                .order(order)
                .product(product)
                .comment(requestDto.getComment())
                .rating(requestDto.getRating())
                .build();

        reviewRepository.save(review);
        return reviewAdapter.maptoGetReviewResponseDto(review);
    }

    public void deleteReviewById(long id){
        getReviewById(id);
        reviewRepository.deleteById(id);
    }
}


