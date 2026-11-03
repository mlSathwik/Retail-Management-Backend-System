package com.retail.management.service;

import com.retail.management.dto.ReviewDTO;
import com.retail.management.entity.Review;
import com.retail.management.exception.ResourceNotFoundException;
import com.retail.management.repository.ProductRepository;
import com.retail.management.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing MongoDB-backed product reviews.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public ReviewDTO createReview(ReviewDTO dto) {
        // Validate that product exists in MySQL catalog
        if (!productRepository.existsById(dto.getProductId())) {
            throw new ResourceNotFoundException("Product not found with id: " + dto.getProductId());
        }

        Review review = Review.builder()
                .productId(dto.getProductId())
                .customerName(dto.getCustomerName())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);
        return mapToDTO(saved);
    }

    public List<ReviewDTO> getReviewsByProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        return reviewRepository.findByProductId(productId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO mapToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .customerName(review.getCustomerName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
