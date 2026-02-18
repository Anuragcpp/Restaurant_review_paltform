package com.project.Restourent.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {

    @NotBlank(message = "Street Number is required")
    @Pattern(regexp = "^[0-9]{1,5}[a-zA-Z]?$", message = "Invalid Street Number Format")
    private String streetNumber;

    @NotBlank(message = "Street Name is required")
    private String streetName;

    @NotBlank(message = "City Name is required")
    private String city;

    @NotBlank(message = "State Name is required")
    private String state;

    @NotBlank(message = "PostalCode is required")
    private String postalCode;

    @NotBlank(message = "Country Name is required")
    private String country;
}
