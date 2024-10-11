package com.example.Sop.datafetchers;

import com.example.Sop.dto.LocationDto;
import com.example.Sop.services.LocationService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;


import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class LocationsDataFetcher {

    LocationService locationService;

    public LocationsDataFetcher(LocationService locationService) {
        this.locationService = locationService;
    }


    @DgsQuery
    public List<LocationDto> locations(@InputArgument String nameFilter) {
        List<LocationDto> locations = locationService.getAllLocations();
        if (nameFilter == null) {
            return locations;
        }

        return locations.stream()
                .filter(location -> location.getName().contains(nameFilter))
                .collect(Collectors.toList());
    }
    @DgsMutation
    public LocationDto addLocation(@InputArgument SubmittedLocation location) {
        LocationDto newLocation = new LocationDto(location.name(),location.address());
        locationService.createLocation(newLocation);
        newLocation.setId(locationService.createLocation(newLocation).getId());
        return newLocation;
    }
    @DgsMutation
    public LocationDto updateLocation(@InputArgument Long id, @InputArgument SubmittedLocation location) {
        LocationDto existingLocation = locationService.getLocationById(id);
        if (existingLocation != null) {
            existingLocation.setName(location.name());
            existingLocation.setAddress(location.address());
            locationService.updateLocation(id, existingLocation);
            return existingLocation;
        }
        return null;
    }


    @DgsMutation
    public void deleteLocation(@InputArgument Long id) {
        locationService.deleteLocation(id);
    }

    record SubmittedLocation(Long id, String name, String address) {}
}


