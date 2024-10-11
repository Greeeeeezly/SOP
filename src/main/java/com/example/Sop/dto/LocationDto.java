package com.example.Sop.dto;

import com.example.Sop.models.Tour;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

public class LocationDto {
    private Long id;
    private String name;
    private String address;
    @JsonManagedReference
    private List<TourDto> tours;

    public LocationDto() {
    }

    public LocationDto(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<TourDto> getTours() {
        return tours;
    }

    public void setTours(List<TourDto> tours) {
        this.tours = tours;
    }

    public LocationDto(Long id, String name, String address, List<TourDto> tours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.tours = tours;
    }
}
