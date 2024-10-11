package com.example.Sop.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.List;

public class TourDto {
    private Long id;

    private String title;
    private String description;
    @JsonBackReference
    private LocationDto location;

    @JsonManagedReference(value = "tour-bookings")
    private List<BookingDto> bookings;

    private LocalDateTime startDate;
    private LocalDateTime endDate;


    public TourDto(String title, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TourDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TourDto(Long id, String title, String description, LocationDto location, List<BookingDto> bookings, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.bookings = bookings;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public List<BookingDto> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDto> bookings) {
        this.bookings = bookings;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public TourDto(String title, String description, LocationDto location, List<BookingDto> bookings, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.bookings = bookings;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
