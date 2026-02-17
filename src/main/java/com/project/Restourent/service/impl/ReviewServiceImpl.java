package com.project.Restourent.service.impl;

import com.project.Restourent.domain.entities.*;
import com.project.Restourent.exception.RestaurantNotFoundException;
import com.project.Restourent.exception.ReviewNotAllowedException;
import com.project.Restourent.repository.RestaurantRepository;
import com.project.Restourent.service.ReviewService;
import kotlin.text.MatchNamedGroupCollection;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

    @Override
    public Page<Review> listReview(String restaurantId, Pageable pageable) {
        Restaurant  restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant Not found with id: "+ restaurantId)
        );
        List<Review> reviews = restaurant.getReviews();
        Sort sort = pageable.getSort();
        if (sort.isSorted()){
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            boolean isAscending = order.getDirection().isAscending();
            Comparator<Review> comparator = switch (property){
                case "datePosted" -> Comparator.comparing(Review::getDataPosted);
                case "Rating" -> Comparator.comparing(Review::getRating);
                default -> Comparator.comparing(Review::getDataPosted);
            };
            reviews.sort(isAscending ? comparator : comparator.reversed());

        }else reviews.sort(Comparator.comparing(Review::getDataPosted).reversed());

        int start = (int) pageable.getOffset();
        if (start >= reviews.size())  return new PageImpl<>(Collections.emptyList(),pageable,reviews.size());
        int  end = Math.min((start+pageable.getPageSize())  , reviews.size());
        return new PageImpl<>(reviews.subList(start,end),pageable, reviews.size());
    }

    @Override
    public Optional<Review> getReview(String restaurantId, String reviewId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                ()-> new RestaurantNotFoundException("Restaurant Not found with id : "+ restaurantId)
        );
        return restaurant.getReviews().stream().filter(review -> review.getId().equals(reviewId)).findFirst();
    }

    @Override
    public Review updateReview(User user, String restaurantId, String reviewId, ReviewCreateUpdateRequest reviewCreateUpdateRequest) {
        return null;
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
