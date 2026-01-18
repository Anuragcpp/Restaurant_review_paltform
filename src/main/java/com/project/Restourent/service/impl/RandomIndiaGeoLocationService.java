package com.project.Restourent.service.impl;

import com.project.Restourent.domain.GeoLocation;
import com.project.Restourent.domain.entities.Address;
import com.project.Restourent.service.GeoLocationService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomIndiaGeoLocationService implements GeoLocationService {

    private static final Float MIN_LATITUDE  = 8.4f;
    private static final Float MAX_LATITUDE  = 37.6f;
    private static final Float MIN_LONGITUDE = 68.7f;
    private static final Float MAX_LONGITUDE = 97.25f;

    @Override
    public GeoLocation getGeoLocation(Address address) {
        Random random = new Random();
        double latitude = MIN_LATITUDE + random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE);
        double longitude = MIN_LONGITUDE +  random.nextDouble() *( MAX_LONGITUDE - MIN_LONGITUDE);

        return GeoLocation.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
