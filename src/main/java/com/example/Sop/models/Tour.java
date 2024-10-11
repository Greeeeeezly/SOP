package com.example.Sop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Tour extends BaseEntity{

    private String title;
    private String description;

    @JoinColumn(name = "location_id")
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)  // Здесь указано каскадное сохранение
    private Location location;

    @OneToMany(mappedBy = "tour")
    @JsonManagedReference(value = "tour-bookings")
    private List<Booking> bookings;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Tour() {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
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
}
