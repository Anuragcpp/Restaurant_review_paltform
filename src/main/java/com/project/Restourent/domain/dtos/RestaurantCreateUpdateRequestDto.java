package com.project.Restourent.domain.dtos;

import com.project.Restourent.domain.entities.Address;
import com.project.Restourent.domain.entities.OperatingHours;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantCreateUpdateRequestDto {

    @NotBlank(message = "Restaurant Name is required")
    private String name;

    @NotBlank(message = "Cuisine-Type is required")
    private String cuisineType;

    @NotBlank(message = "Contact Information is required")
    private String  contactInformation;

    @Valid
    private Address address;

    @Valid
    private OperatingHours operatingHours;

    @Size(min = 1, message = "Minimum one Photo ID is required")
    private List<String> photoIds;
}
