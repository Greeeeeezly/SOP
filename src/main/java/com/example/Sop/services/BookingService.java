// src/main/java/com/example/Sop/services/BookingService.java
package com.example.Sop.services;

import com.example.Sop.dto.BookingDto;
import com.example.Sop.models.Booking;
import com.example.Sop.models.Customer;
import com.example.Sop.models.Tour;
import com.example.Sop.repositories.BookingRepository;
import com.example.Sop.repositories.CustomerRepository;
import com.example.Sop.repositories.TourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final TourRepository tourRepository;
    private final ModelMapper modelMapper;

    public BookingService(BookingRepository bookingRepository, CustomerRepository customerRepository, TourRepository tourRepository, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.tourRepository = tourRepository;
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

    public BookingDto createBooking(BookingDto bookingDto) {
        Booking booking = modelMapper.map(bookingDto, Booking.class);

        Customer customer = customerRepository.findById(bookingDto.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + bookingDto.getCustomer().getId()));
        booking.setCustomer(customer);

        Tour tour = tourRepository.findById(bookingDto.getTour().getId())
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + bookingDto.getTour().getId()));
        booking.setTour(tour);

        Booking savedBooking = bookingRepository.save(booking);

        return modelMapper.map(savedBooking, BookingDto.class);
    }

    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        existingBooking.setBookingDate(bookingDto.getBookingDate());
        existingBooking.setActive(bookingDto.isActive());

        if (!existingBooking.getCustomer().getId().equals(bookingDto.getCustomer().getId())) {
            Customer newCustomer = customerRepository.findById(bookingDto.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + bookingDto.getCustomer().getId()));
            existingBooking.setCustomer(newCustomer);
        }

        if (!existingBooking.getTour().getId().equals(bookingDto.getTour().getId())) {
            Tour newTour = tourRepository.findById(bookingDto.getTour().getId())
                    .orElseThrow(() -> new RuntimeException("Tour not found with id: " + bookingDto.getTour().getId()));
            existingBooking.setTour(newTour);
        }

        Booking updatedBooking = bookingRepository.save(existingBooking);

        return modelMapper.map(updatedBooking, BookingDto.class);
    }


    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        bookingRepository.delete(booking);
    }
}
