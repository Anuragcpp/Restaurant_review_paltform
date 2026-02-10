package com.project.Restourent.service.impl;

import com.project.Restourent.domain.GeoLocation;
import com.project.Restourent.domain.RestaurantCreateUpdateRequest;
import com.project.Restourent.domain.entities.Address;
import com.project.Restourent.domain.entities.Photo;
import com.project.Restourent.domain.entities.Restaurant;
import com.project.Restourent.repository.RestaurantRepository;
import com.project.Restourent.service.GeoLocationService;
import com.project.Restourent.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private GeoLocationService geoLocationService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Restaurant createRestaurant(RestaurantCreateUpdateRequest request) {
        GeoLocation geoLocation = geoLocationService.getGeoLocation(request.getAddress());
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(),geoLocation.getLongitude());
        List<Photo> photos = request.getPhotoIds().stream().map(photoUrl -> Photo.builder()
                .url(photoUrl)
                .uploadedDate(LocalDateTime.now())
                .build()).toList();
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .cuisineType(request.getCuisineType())
                .address(request.getAddress())
                .createdBy(request.getCreatedBy())
                .contactInformation(request.getContactInformation())
                .operatingHours(request.getOperatingHours())
                .geoLocation(geoPoint)
                .averageRating(0f)
                .photos(photos)
                .build();
        return  restaurantRepository.save(restaurant);
    }

    @Override
    public Page<Restaurant> searchRestaurants(String query, Float minRating, Float latitude, Float longitude, Float radius, Pageable pageable) {

        if (minRating != null && ( query == null || query.isEmpty())) {
            return restaurantRepository.findByAverageRatingGreaterThanEqual(minRating,pageable);
        }

        Float searchMinRating = minRating == null ? 0f : minRating;
        if (query != null && query.trim().isBlank() ){
            return restaurantRepository.findByQueryAndMinRating(query,searchMinRating,pageable);
        }

        if (latitude != null && longitude != null && radius != null ){
            return restaurantRepository.findByLocationNear(latitude,longitude,radius,pageable);
        }

        return restaurantRepository.findAll(pageable);
    }

    @Override
    public Optional<Restaurant> findRestaurantById(String id) {
        return restaurantRepository.findById(id);
    }

}
