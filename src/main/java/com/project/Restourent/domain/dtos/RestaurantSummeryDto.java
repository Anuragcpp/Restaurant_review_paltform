package com.project.Restourent.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantSummeryDto {
    private String id;
    private String name;
    private String cuisineType;
    private Float averageRating;
    private Integer totalReviews;
    private AddressDto addressDto;
    private List<PhotoDto> photos;
}
