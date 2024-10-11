package com.example.Sop.datafetchers;

import com.example.Sop.dto.BookingDto;
import com.example.Sop.dto.CustomerDto;
import com.example.Sop.dto.TourDto;
import com.example.Sop.services.BookingService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

    @DgsComponent
    public class BookingDataFetcher {

        private final BookingService bookingService;

        @Autowired
        public BookingDataFetcher(BookingService bookingService) {
            this.bookingService = bookingService;
        }

        @DgsQuery
        public List<BookingDto> bookings() {
            return bookingService.getAllBookings();
        }

        @DgsMutation
        public BookingDto addBooking(@InputArgument SubmittedBooking booking) {
            BookingDto newBooking = new BookingDto();
            CustomerDto customerDto = booking.customer();
            TourDto tourDto = booking.tour();
            newBooking.setCustomer(customerDto);
            newBooking.setTour(tourDto);
            newBooking.setBookingDate(booking.bookingDate());
            newBooking.setActive(booking.active());

            return bookingService.createBooking(newBooking);
        }

        @DgsMutation
        public BookingDto updateBooking(@InputArgument Long id, @InputArgument SubmittedBooking booking) {
            BookingDto existingBooking = bookingService.getBookingById(id);
            if (existingBooking != null) {
                existingBooking.setCustomer(booking.customer());
                existingBooking.setTour(booking.tour());
                existingBooking.setBookingDate(booking.bookingDate());
                existingBooking.setActive(booking.active());
                return bookingService.updateBooking(id, existingBooking);
            }
            return null;
        }

        @DgsMutation
        public void deleteBooking(@InputArgument Long id) {
            bookingService.deleteBooking(id);
        }

        record SubmittedBooking(CustomerDto customer, TourDto tour, LocalDateTime bookingDate, boolean active) {}
    }
