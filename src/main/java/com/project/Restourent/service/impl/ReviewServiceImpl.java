package com.project.Restourent.service.impl;

import com.project.Restourent.domain.entities.*;
import com.project.Restourent.exception.RestaurantNotFoundException;
import com.project.Restourent.exception.ReviewNotAllowedException;
import com.project.Restourent.repository.RestaurantRepository;
import com.project.Restourent.service.ReviewService;
import kotlin.text.MatchNamedGroupCollection;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        Restaurant restaurant = getRestaurantOrThrough(restaurantId);
        return getReviewFromRestaurant(reviewId, restaurant);
    }


    @Override
    public Review updateReview(User user, String restaurantId, String reviewId, ReviewCreateUpdateRequest reviewCreateUpdateRequest) {
        Restaurant restaurant = getRestaurantOrThrough(restaurantId);
        String authorId = user.getId();
        Review existingReview = getReviewFromRestaurant(reviewId,restaurant).orElseThrow(
                () -> new ReviewNotAllowedException("Review Does not Exist")
        );
        if(!authorId.equals(existingReview.getWrittenBy().getId())) throw  new ReviewNotAllowedException("Can't update another user's review");
        if(LocalDateTime.now().isAfter(existingReview.getDataPosted().plusHours(48))) throw  new ReviewNotAllowedException("Review can no longer be edited");

        existingReview.setContent(reviewCreateUpdateRequest.getContent());
        existingReview.setRating(reviewCreateUpdateRequest.getRating());
        existingReview.setLastEdited(LocalDateTime.now());
        existingReview.setPhots(
                reviewCreateUpdateRequest
                        .getPhotoIds()
                        .stream()
                        .map(
                                photo -> Photo
                                        .builder()
                                        .url(photo)
                                        .uploadedDate(LocalDateTime.now())
                                        .build())
                        .toList());

        updateAverageRating(restaurant);

        List<Review> updatedReviews = restaurant.getReviews().stream().filter(r->  !r.getId().equals(reviewId)).collect(Collectors.toList());
        updatedReviews.add(existingReview);
        restaurant.setReviews(updatedReviews);
        restaurantRepository.save(restaurant);
        return existingReview;
    }

    @Override
    public void deleteReview(User user, String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrough(restaurantId);
        Review review = getReviewFromRestaurant(reviewId,restaurant).orElseThrow(
                () -> new ReviewNotAllowedException("Review not Found with Id : "+ reviewId)
        );
        if (!review.getWrittenBy().getId().equals(user.getId())) throw new ReviewNotAllowedException("Can't delete other user's review");
        List<Review> updatedReview = restaurant.getReviews().stream().filter(r -> !r.getId().equals(reviewId)).toList();
        restaurant.setReviews(updatedReview);
        updateAverageRating(restaurant);
        restaurantRepository.save(restaurant);
    }

    @NotNull
    private static Optional<Review> getReviewFromRestaurant(String reviewId, Restaurant restaurant) {
        return restaurant.getReviews().stream().filter(review -> review.getId().equals(reviewId)).findFirst();
    }

    private Restaurant getRestaurantOrThrough(String restaurantId){
        return restaurantRepository.findById(restaurantId).orElseThrow(
                ()-> new RestaurantNotFoundException("Restaurant Not found with ID : "+ restaurantId)
        );
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
