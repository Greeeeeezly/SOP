package com.example.Sop.datafetchers;

import com.example.Sop.dto.BookingDto;
import com.example.Sop.dto.UserDto;
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

        public BookingDataFetcher(BookingService bookingService) {
            this.bookingService = bookingService;
        }

        @DgsQuery
        public List<BookingDto> bookings() {
            return bookingService.getAllBookings();
        }

        @DgsMutation
        public String addBooking(@InputArgument SubmittedBooking booking) {
            BookingDto newBooking = new BookingDto();
            UserDto userDto = booking.user();
            TourDto tourDto = booking.tour();
            newBooking.setTour(tourDto);
            return bookingService.createBooking(tourDto.getId(),userDto.getId());
        }

        @DgsMutation
        public void deleteBooking(@InputArgument Long id) {
            bookingService.deleteBooking(id);
        }

        record SubmittedBooking(UserDto user, TourDto tour) {}
    }
