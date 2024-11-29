package com.example.Sop.controllers;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping
    public RepresentationModel<?> root() {
        RepresentationModel<?> model = new RepresentationModel<>();

        // Добавляем ссылки на возможные действия
        model.add(linkTo(methodOn(UserController.class).getAllCustomers()).withRel("customers"));
        model.add(linkTo(methodOn(TourController.class).getAllTours()).withRel("tours"));
        model.add(linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"));

        return model;
    }
}
