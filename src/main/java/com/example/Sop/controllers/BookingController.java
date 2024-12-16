package com.example.Sop.controllers;

import com.example.Sop.ActionModel;
import com.example.Sop.services.BookingService;
import com.example.excursionbookingapi.dto.BookingDto;
import com.example.excursionbookingapi.dto.BookingRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/bookings")
public class BookingController implements com.example.excursionbookingapi.controllers.BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public CollectionModel<EntityModel<BookingDto>> getAllBookings() {
        List<EntityModel<BookingDto>> bookings = bookingService.getAllBookings().stream()
                .map(booking -> {
                    EntityModel<BookingDto> bookingModel = EntityModel.of(booking,
                            linkTo(methodOn(BookingController.class).getBookingById(booking.getId())).withSelfRel());

                    // Создаем actions
                    ActionModel deleteAction = new ActionModel("delete", "DELETE",
                            linkTo(methodOn(BookingController.class).deleteBooking(booking.getId())).withSelfRel());

                    // Добавляем действия к модели
                    bookingModel.add(
                            linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"),
                            deleteAction.getLink().withRel(deleteAction.getName()).withType(deleteAction.getMethod())
                    );

                    return bookingModel;
                })
                .collect(Collectors.toList());

        return CollectionModel.of(bookings,
                linkTo(methodOn(BookingController.class).getAllBookings()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<BookingDto> getBookingById(@PathVariable Long id) {
        BookingDto booking = bookingService.getBookingById(id);

        // Создаем actions
        ActionModel deleteAction = new ActionModel("delete", "DELETE",
                linkTo(methodOn(BookingController.class).deleteBooking(id)).withSelfRel());

        return EntityModel.of(booking,
                linkTo(methodOn(BookingController.class).getBookingById(id)).withSelfRel(),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"),
                deleteAction.getLink().withRel(deleteAction.getName()).withType(deleteAction.getMethod())
        );
    }

    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody BookingRequest bookingRequest) {
        String action = bookingService.createBooking(bookingRequest.getTourId(), bookingRequest.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(action);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
