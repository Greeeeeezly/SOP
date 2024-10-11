package com.example.Sop.controllers;

import com.example.Sop.ActionModel;
import com.example.Sop.dto.BookingDto;
import com.example.Sop.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public CollectionModel<EntityModel<BookingDto>> getAllBookings() {
        List<EntityModel<BookingDto>> bookings = bookingService.getAllBookings().stream()
                .map(booking -> {
                    EntityModel<BookingDto> bookingModel = EntityModel.of(booking,
                            linkTo(methodOn(BookingController.class).getBookingById(booking.getId())).withSelfRel());

                    // Создаем actions
                    ActionModel updateAction = new ActionModel("update", "POST",
                            linkTo(methodOn(BookingController.class).updateBooking(booking.getId(), null)).withSelfRel());
                    ActionModel deleteAction = new ActionModel("delete", "DELETE",
                            linkTo(methodOn(BookingController.class).deleteBooking(booking.getId())).withSelfRel());

                    // Добавляем действия к модели
                    bookingModel.add(
                            linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"),
                            updateAction.getLink().withRel(updateAction.getName()).withType(updateAction.getMethod()),
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
        ActionModel updateAction = new ActionModel("update", "POST",
                linkTo(methodOn(BookingController.class).updateBooking(id, null)).withSelfRel());
        ActionModel deleteAction = new ActionModel("delete", "DELETE",
                linkTo(methodOn(BookingController.class).deleteBooking(id)).withSelfRel());

        return EntityModel.of(booking,
                linkTo(methodOn(BookingController.class).getBookingById(id)).withSelfRel(),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"),
                updateAction.getLink().withRel(updateAction.getName()).withType(updateAction.getMethod()),
                deleteAction.getLink().withRel(deleteAction.getName()).withType(deleteAction.getMethod())
        );
    }

    @PostMapping
    public EntityModel<BookingDto> createBooking(@RequestBody BookingDto booking) {
        BookingDto createdBooking = bookingService.createBooking(booking);
        return EntityModel.of(createdBooking,
                linkTo(methodOn(BookingController.class).getBookingById(createdBooking.getId())).withSelfRel(),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"));
    }

    @PostMapping("/{id}")
    public EntityModel<BookingDto> updateBooking(@PathVariable Long id, @RequestBody BookingDto updatedBooking) {
        BookingDto booking = bookingService.updateBooking(id, updatedBooking);
        return EntityModel.of(booking,
                linkTo(methodOn(BookingController.class).getBookingById(booking.getId())).withSelfRel(),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
