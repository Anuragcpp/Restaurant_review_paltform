package com.project.Restourent.service.impl;

import com.project.Restourent.domain.entities.*;
import com.project.Restourent.exception.RestaurantNotFoundException;
import com.project.Restourent.exception.ReviewNotAllowedException;
import com.project.Restourent.repository.RestaurantRepository;
import com.project.Restourent.service.ReviewService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;


    @Override
    public Review createReview(User user, String restaurantId, ReviewCreateUpdateRequest review) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id not found: " +  restaurantId));

        boolean hasExistingReview = restaurant.getReviews().stream().anyMatch(
                r -> r.getWrittenBy().getId().equals(user.getId())
        );
        if (hasExistingReview) throw new ReviewNotAllowedException("User Has already Reviewed this Restaurant");
        LocalDateTime now = LocalDateTime.now();
        List<Photo> photos = review.getPhotoIds().stream().map(
                url ->  Photo
                        .builder()
                        .url(url)
                        .uploadedDate(now)
                        .build()
        ).toList();

        Review reviewToCreate = Review
                .builder()
                .id(UUID.randomUUID().toString())
                .content(review.getContent())
                .dataPosted(now)
                .lastEdited(now)
                .phots(photos)
                .writtenBy(user)
                .build();

        restaurant.getReviews().add(reviewToCreate);

        updateAverageRating(restaurant);

        restaurantRepository.save(restaurant);
        return reviewToCreate;
    }

    private void updateAverageRating(Restaurant restaurant){
        List<Review>  reviews = restaurant.getReviews();
        if (reviews.isEmpty()) restaurant.setAverageRating(0.0f);
        else {
            double averageRating =  reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            restaurant.setAverageRating((float)averageRating);
        }
    }
}
