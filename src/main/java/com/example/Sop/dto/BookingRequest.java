package com.example.Sop.dto;

public class BookingRequest {
    private Long tourId;
    private Long userId;

    public BookingRequest() {}

    public BookingRequest(Long tourId, Long userId) {
        this.tourId = tourId;
        this.userId = userId;
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
