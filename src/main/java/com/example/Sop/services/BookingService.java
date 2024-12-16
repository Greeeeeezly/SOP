package com.example.Sop.services;
import com.example.Sop.models.Booking;
import com.example.Sop.models.Subscription;
import com.example.Sop.models.Tour;
import com.example.Sop.models.User;
import com.example.Sop.repositories.BookingRepository;
import com.example.Sop.repositories.SubscriptionRepository;
import com.example.Sop.repositories.TourRepository;
import com.example.Sop.repositories.UserRepository;
import com.example.excursionbookingapi.dto.BookingDto;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final RabbitTemplate rabbitTemplate;
    private final TourRepository tourRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final ModelMapper modelMapper;

    public BookingService(RabbitTemplate rabbitTemplate, TourRepository tourRepository, SubscriptionRepository subscriptionRepository, UserRepository userRepository, BookingRepository bookingRepository, ModelMapper modelMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.tourRepository = tourRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
    }

    public List<BookingDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return modelMapper.map(booking, BookingDto.class);
    }

    public String createBooking(Long tourId, Long userId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NoSuchElementException("Tour with ID " + tourId + " not found"));
        if (tour.getAvailableSeats() > 0) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
            tour.setAvailableSeats(tour.getAvailableSeats() - 1);
            tourRepository.save(tour);
            Booking booking = new Booking(user, tour);
            bookingRepository.save(booking);
            return "Booking";
        } else {
            createSubscription(tourId, userId);
            return "Subscription";
        }
    }

    public void createSubscription(Long tourId, Long userId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NoSuchElementException("Tour with ID " + tourId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
        Subscription subscription = new Subscription(user, tour);
        subscriptionRepository.save(subscription);
    }

    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking with ID " + bookingId + " not found"));
        Tour tour = booking.getTour();
        bookingRepository.delete(booking);

        rabbitTemplate.convertAndSend("notificationExchange", "notification.priority", tour.getId());
        rabbitTemplate.convertAndSend("notificationExchange", "notification.wspriority", "booking deleted in tour" + tour.getId().toString() + " user: " + booking.getUser().getId());
    }
}
