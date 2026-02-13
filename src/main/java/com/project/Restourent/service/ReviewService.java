package com.project.Restourent.service;

import com.project.Restourent.domain.entities.Review;
import com.project.Restourent.domain.entities.ReviewCreateUpdateRequest;
import com.project.Restourent.domain.entities.User;

public interface ReviewService {
    public Review createReview(User user, String restaurantId, ReviewCreateUpdateRequest review);
}
