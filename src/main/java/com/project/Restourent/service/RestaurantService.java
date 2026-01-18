package com.project.Restourent.service;

import com.project.Restourent.domain.RestaurantCreateUpdateRequest;
import com.project.Restourent.domain.entities.Restaurant;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateRequest  request);
}
