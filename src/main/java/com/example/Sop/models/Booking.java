package com.example.Sop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Booking extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference(value = "customer-bookings")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    @JsonBackReference(value = "tour-bookings")
    private Tour tour;

    private LocalDateTime bookingDate;

    private boolean isActive;

    public Booking() {
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
}
