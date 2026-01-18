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
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.LocalDateTime;
import java.util.List;

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
                .contactInformation(request.getContactInformation())
                .geoLocation(geoPoint)
                .averageRating(0f)
                .photos(photos)
                .build();
        return  restaurantRepository.save(restaurant);
    }

}
