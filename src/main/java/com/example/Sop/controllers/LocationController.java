package com.example.Sop.controllers;

import com.example.Sop.ActionModel;
import com.example.Sop.dto.CustomerDto;
import com.example.Sop.dto.LocationDto;
import com.example.Sop.models.Location;
import com.example.Sop.services.LocationService;
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
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public CollectionModel<EntityModel<LocationDto>> getAllLocations() {
        List<EntityModel<LocationDto>> locations = locationService.getAllLocations().stream()
                .map(location -> {
                    // Создаем ссылку на сущность Location (self link)
                    EntityModel<LocationDto> locationModel = EntityModel.of(location,
                            linkTo(methodOn(LocationController.class).getLocationById(location.getId())).withSelfRel());

                    // Создаем ActionModel для update
                    ActionModel updateAction = new ActionModel("update", "POST",
                            linkTo(methodOn(LocationController.class).updateLocation(location.getId(), null)).withSelfRel());

                    // Создаем ActionModel для delete
                    ActionModel deleteAction = new ActionModel("delete", "DELETE",
                            linkTo(methodOn(LocationController.class).deleteLocation(location.getId())).withSelfRel());

                    // Добавляем ссылки на действия и связанные ресурсы
                    locationModel.add(
                            linkTo(methodOn(LocationController.class).getAllLocations()).withRel("locations"),
                            updateAction.getLink().withRel(updateAction.getName()).withType(updateAction.getMethod()),
                            deleteAction.getLink().withRel(deleteAction.getName()).withType(deleteAction.getMethod()),
                            linkTo(methodOn(TourController.class).getAllTours()).withRel("tours"),
                            linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings")
                    );

                    return locationModel;
                })
                .collect(Collectors.toList());

        return CollectionModel.of(locations,
                linkTo(methodOn(LocationController.class).getAllLocations()).withSelfRel());
    }


    @GetMapping("/{id}")
    public EntityModel<LocationDto> getLocationById(@PathVariable Long id) {
        LocationDto location = locationService.getLocationById(id);
        return EntityModel.of(location,
                linkTo(methodOn(LocationController.class).getLocationById(id)).withSelfRel(),
                linkTo(methodOn(LocationController.class).getAllLocations()).withRel("locations"));
    }

    @PostMapping
    public EntityModel<LocationDto> createLocation(@RequestBody LocationDto location) {
        LocationDto createdLocation = locationService.createLocation(location);
        return EntityModel.of(createdLocation,
                linkTo(methodOn(LocationController.class).getLocationById(createdLocation.getId())).withSelfRel(),
                linkTo(methodOn(LocationController.class).getAllLocations()).withRel("locations"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}")
    public EntityModel<LocationDto> updateLocation(@PathVariable Long id, @RequestBody LocationDto updatedLocation) {
        LocationDto location = locationService.updateLocation(id, updatedLocation);
        return EntityModel.of(location,
                linkTo(methodOn(LocationController.class).getLocationById(location.getId())).withSelfRel(),
                linkTo(methodOn(LocationController.class).getAllLocations()).withRel("customers"));
    }
}
