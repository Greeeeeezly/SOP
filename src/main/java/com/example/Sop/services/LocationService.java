package com.example.Sop.services;

import com.example.Sop.dto.LocationDto;
import com.example.Sop.models.Location;
import com.example.Sop.repositories.LocationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    public LocationService(LocationRepository locationRepository, ModelMapper modelMapper) {
        this.locationRepository = locationRepository;
        this.modelMapper = modelMapper;
    }

    public List<LocationDto> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream()
                .map(location -> modelMapper.map(location, LocationDto.class))
                .collect(Collectors.toList());
    }

    public LocationDto getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
        return modelMapper.map(location, LocationDto.class);
    }

    public LocationDto createLocation(LocationDto locationDto) {
        Location location = modelMapper.map(locationDto, Location.class);
        Location savedLocation = locationRepository.save(location);
        return modelMapper.map(savedLocation, LocationDto.class);
    }

    public LocationDto updateLocation(Long id, LocationDto locationDto) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));

        existingLocation.setName(locationDto.getName());
        existingLocation.setAddress(locationDto.getAddress());

        Location updatedLocation = locationRepository.save(existingLocation);
        return modelMapper.map(updatedLocation, LocationDto.class);
    }

    public void deleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
        locationRepository.delete(location);
    }
}
