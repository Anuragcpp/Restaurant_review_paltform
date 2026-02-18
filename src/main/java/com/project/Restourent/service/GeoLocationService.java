package com.project.Restourent.service;

import com.project.Restourent.domain.GeoLocation;
import com.project.Restourent.domain.entities.Address;

public interface GeoLocationService {
    public GeoLocation getGeoLocation(Address address);
}
