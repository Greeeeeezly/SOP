package com.example.Sop.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;

public class BookingDto implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @JsonBackReference
    private UserDto user;
    @JsonBackReference(value = "tour-bookings")
    private TourDto tour;


    public TourDto getTour() {
        return tour;
    }

    public void setTour(TourDto tour) {
        this.tour = tour;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }


    public BookingDto(UserDto user, TourDto tour) {
        this.user = user;
        this.tour = tour;
    }

    public BookingDto(Long id, UserDto user, TourDto tour) {
        this.id = id;
        this.user = user;
        this.tour = tour;
    }

    public BookingDto() {
    }

    @Override
    public String toString() {
        return "BookingDto{" +
                "id=" + id +
                ", tour=" + tour +
                '}';
    }
}
