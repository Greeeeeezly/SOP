package com.example.Sop.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;

public class BookingDto {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @JsonBackReference
    private CustomerDto customer;
    @JsonBackReference(value = "tour-bookings")
    private TourDto tour;

    private LocalDateTime bookingDate;

    private boolean isActive;

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public TourDto getTour() {
        return tour;
    }

    public void setTour(TourDto tour) {
        this.tour = tour;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public BookingDto(Long id, CustomerDto customer, TourDto tour, LocalDateTime bookingDate, boolean isActive) {
        this.id=id;
        this.customer = customer;
        this.tour = tour;
        this.bookingDate = bookingDate;
        this.isActive = isActive;
    }

    public BookingDto() {
    }
}
