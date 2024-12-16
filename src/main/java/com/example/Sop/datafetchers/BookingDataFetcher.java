package com.example.Sop.datafetchers;

import com.example.Sop.services.BookingService;
import com.example.excursionbookingapi.dto.BookingDto;
import com.example.excursionbookingapi.dto.TourDto;
import com.example.excursionbookingapi.dto.UserDto;
import com.example.excursionbookingapi.exceptions.BookingNotFoundException;
import com.example.excursionbookingapi.exceptions.InvalidArgumentException;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;

@DgsComponent
    public class BookingDataFetcher implements com.example.excursionbookingapi.datafetchers.BookingDataFetcher {

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
    }
