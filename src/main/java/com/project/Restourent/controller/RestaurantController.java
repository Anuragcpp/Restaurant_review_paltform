package com.project.Restourent.controller;

import com.project.Restourent.domain.RestaurantCreateUpdateRequest;
import com.project.Restourent.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.project.Restourent.domain.dtos.RestaurantDto;
import com.project.Restourent.domain.dtos.response.ApiResponse;
import com.project.Restourent.domain.entities.Restaurant;
import com.project.Restourent.mapper.RestaurantMappers;
import com.project.Restourent.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantMappers restaurantMappers;

    @PostMapping
    public ResponseEntity<ApiResponse> createRestaurant(
            @RequestBody @Valid RestaurantCreateUpdateRequestDto request
            ) {
        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMappers.toRestaurantCreateUpdateRequest(request);
        Restaurant restaurant =  restaurantService.createRestaurant(restaurantCreateUpdateRequest);
        RestaurantDto restaurantDto = restaurantMappers.toRestaurantDto(restaurant);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK.value(),
                        "Restaurant Create Successfully",
                        restaurantDto
                ),
                HttpStatus.OK
        );
    }
}
