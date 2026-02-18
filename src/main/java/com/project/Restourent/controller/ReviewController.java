package com.project.Restourent.controller;

import com.project.Restourent.domain.dtos.ReviewCreateUpdateRequestDto;
import com.project.Restourent.domain.dtos.ReviewDto;
import com.project.Restourent.domain.dtos.response.ApiResponse;
import com.project.Restourent.domain.entities.Review;
import com.project.Restourent.domain.entities.ReviewCreateUpdateRequest;
import com.project.Restourent.domain.entities.User;
import com.project.Restourent.exception.ReviewNotFoundException;
import com.project.Restourent.mapper.ReviewMapper;
import com.project.Restourent.service.ReviewService;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurant_id}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ApiResponse> createReiver(
            @PathVariable(name = "restaurant_id") String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt
            ){
        ReviewCreateUpdateRequest request = reviewMapper.toReviewCreateUpdateRequest(review);
        User user = jwtToUser(jwt);
        Review createdReview = reviewService.createReview(user,restaurantId,request);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK.value(),
                        "Review Created Successfully",
                        reviewMapper.toDto(createdReview)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping
    public Page<ReviewDto>  listReviews (
            @PathVariable(name ="restaurant_id") String restaurantId,
            @PageableDefault(
                    size = 20,
                    page = 0,
                    sort = "datePosted",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ){
        return reviewService.listReview(restaurantId,pageable).map(reviewMapper::toDto);
    }

    @GetMapping("/{review_id}")
    public ResponseEntity<ApiResponse> getReview(
            @PathVariable(name = "restaurant_id") String restaurantId,
            @PathVariable(name = "review_id") String reviewId
    ){
        Review review = reviewService.getReview(restaurantId,reviewId).orElseThrow(
                () -> new ReviewNotFoundException("Review Not found with ID : "+ reviewId)
        );
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK.value(),
                        "Review Retrieve Successfully",
                        review
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/{review_id}")
    public ResponseEntity<ApiResponse> updateReview(
            @PathVariable(name = "review_id") String reviewId,
            @PathVariable(name = "restaurant_id") String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt
    ){
        Review updatedReview = reviewService.updateReview(
                jwtToUser(jwt),
                restaurantId,
                reviewId,
                reviewMapper.toReviewCreateUpdateRequest(review)
        );
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK.value(),
                        "Review Updated Successfully",
                        updatedReview
                ),
                HttpStatus.OK);
        
    }

    private User jwtToUser(Jwt jwt){
        return User.builder()
                .id(jwt.getSubject())
                .userName(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .givenName(jwt.getClaimAsString("family_name"))
                .build();
    }
}
