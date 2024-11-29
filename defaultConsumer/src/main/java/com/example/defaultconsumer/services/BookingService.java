package com.example.defaultconsumer.services;


import com.example.defaultconsumer.models.Booking;
import com.example.defaultconsumer.models.Tour;
import com.example.defaultconsumer.models.User;
import com.example.defaultconsumer.repositiries.BookingRepository;
import com.example.defaultconsumer.repositiries.SubscriptionRepository;
import com.example.defaultconsumer.repositiries.TourRepository;
import com.example.defaultconsumer.repositiries.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingService {

    private final RabbitTemplate rabbitTemplate;
    private final TourRepository tourRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public BookingService(RabbitTemplate rabbitTemplate, TourRepository tourRepository, SubscriptionRepository subscriptionRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.tourRepository = tourRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking with ID " + bookingId + " not found"));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void createBooking(Long tourId, Long userId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NoSuchElementException("Tour with ID " + tourId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
        Booking booking = new Booking(user, tour);
        bookingRepository.save(booking);
    }
}
