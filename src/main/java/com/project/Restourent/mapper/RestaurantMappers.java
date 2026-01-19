package com.project.Restourent.mapper;

import com.project.Restourent.domain.RestaurantCreateUpdateRequest;
import com.project.Restourent.domain.dtos.GeoPointDto;
import com.project.Restourent.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.project.Restourent.domain.dtos.RestaurantDto;
import com.project.Restourent.domain.entities.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMappers {
    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest (RestaurantCreateUpdateRequestDto restaurantCreateUpdateRequestDto);

    RestaurantDto toRestaurantDto(Restaurant restaurant);

    @Mapping(target = "latitude",expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude",expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);
}
