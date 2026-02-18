package com.project.Restourent.mapper;

import com.project.Restourent.domain.RestaurantCreateUpdateRequest;
import com.project.Restourent.domain.dtos.GeoPointDto;
import com.project.Restourent.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.project.Restourent.domain.dtos.RestaurantDto;
import com.project.Restourent.domain.dtos.RestaurantSummeryDto;
import com.project.Restourent.domain.entities.Restaurant;
import com.project.Restourent.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMappers {
    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest (RestaurantCreateUpdateRequestDto restaurantCreateUpdateRequestDto);

    @Mapping(source = "reviews",target = "totalReviews", qualifiedByName = "populateTotalReview")
    RestaurantDto toRestaurantDto(Restaurant restaurant);

    @Mapping(target = "latitude",expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude",expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);

    @Mapping(source = "reviews",target = "totalReviews", qualifiedByName = "populateTotalReview")
    RestaurantSummeryDto toRestaurantSummeryDto(Restaurant restaurant);

    @Named("populateTotalReview")
    default Integer populateTotalReview(List<Review> reviews){
        if (reviews == null )return 0;
        return reviews.size();
    }
}
