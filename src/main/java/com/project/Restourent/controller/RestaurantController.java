package com.project.Restourent.controller;

import com.project.Restourent.domain.RestaurantCreateUpdateRequest;
import com.project.Restourent.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.project.Restourent.domain.dtos.RestaurantDto;
import com.project.Restourent.domain.dtos.RestaurantSummeryDto;
import com.project.Restourent.domain.dtos.response.ApiResponse;
import com.project.Restourent.domain.entities.Restaurant;
import com.project.Restourent.mapper.RestaurantMappers;
import com.project.Restourent.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping
    public Page<RestaurantSummeryDto> searchRestaurant(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float latitude,
            @RequestParam(required = false)Float longitude,
            @RequestParam(required = false)Float radius,
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "20")int size
    ){
        Page<Restaurant> searchResults = restaurantService.searchRestaurants(
                q,minRating,latitude,longitude,radius, PageRequest.of(page-1,size)
        );
        return searchResults.map(restaurantMappers::toRestaurantSummeryDto);
    }

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<ApiResponse> getRestaurantById(@PathVariable(name = "restaurant_id") String id){
        Optional<Restaurant> restaurant = restaurantService.findRestaurantById(id);
        if (restaurant.isPresent()){
            //found
            return new ResponseEntity<>(
                    new ApiResponse(
                            HttpStatus.OK.value(),
                            "No Restaurant found with id " + id,
                            restaurant.stream().map(restaurantMappers::toRestaurantDto)
                    ),
                    HttpStatus.OK
            );
        }
            //not fond
            return new ResponseEntity<>(
                    new ApiResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "No Restaurant found with id " + id,
                        null
                    ),
                    HttpStatus.NOT_FOUND);
    }
}
