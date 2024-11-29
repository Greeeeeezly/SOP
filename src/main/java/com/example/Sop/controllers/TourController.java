package com.example.Sop.controllers;

import com.example.Sop.dto.TourDto;
import com.example.Sop.services.TourService;
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
@RequestMapping("/tours")
public class TourController {

    private TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public CollectionModel<EntityModel<TourDto>> getAllTours() {
        List<EntityModel<TourDto>> tours = tourService.getAllTours().stream()
                .map(tour -> EntityModel.of(tour,
                        linkTo(methodOn(TourController.class).getTourById(tour.getId())).withSelfRel(),
                        linkTo(methodOn(TourController.class).getAllTours()).withRel("tours")))
                .collect(Collectors.toList());

        return CollectionModel.of(tours,
                linkTo(methodOn(TourController.class).getAllTours()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<TourDto> getTourById(@PathVariable Long id) {
        TourDto tour = tourService.getTourById(id);
        return EntityModel.of(tour,
                linkTo(methodOn(TourController.class).getTourById(id)).withSelfRel(),
                linkTo(methodOn(TourController.class).getAllTours()).withRel("tours"));
    }

    @PostMapping
    public EntityModel<TourDto> createTour(@RequestBody TourDto tour) {
        TourDto createdTour = tourService.createTour(tour);
        return EntityModel.of(createdTour,
                linkTo(methodOn(TourController.class).getTourById(createdTour.getId())).withSelfRel(),
                linkTo(methodOn(TourController.class).getAllTours()).withRel("tours"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return ResponseEntity.noContent().build();
    }
}
