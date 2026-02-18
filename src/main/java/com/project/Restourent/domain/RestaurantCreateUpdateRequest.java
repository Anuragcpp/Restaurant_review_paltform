package com.project.Restourent.domain;

import com.project.Restourent.domain.entities.Address;
import com.project.Restourent.domain.entities.OperatingHours;
import com.project.Restourent.domain.entities.Photo;
import com.project.Restourent.domain.entities.User;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantCreateUpdateRequest {
    private String name;
    private String cuisineType;
    private String  contactInformation;
    private Address address;
    private User createdBy;
    private OperatingHours operatingHours;
    private List<String> photoIds;
}
