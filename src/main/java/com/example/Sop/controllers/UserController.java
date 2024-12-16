package com.example.Sop.controllers;

import com.example.Sop.ActionModel;
import com.example.Sop.services.UserService;
import com.example.excursionbookingapi.dto.UserDto;
import com.example.excursionbookingapi.dto.UserRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController implements com.example.excursionbookingapi.controllers.UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getAllCustomers() {
        List<EntityModel<UserDto>> customers = userService.getAllUsers().stream()
                .map(customer -> {
                    EntityModel<UserDto> customerModel = EntityModel.of(customer,
                            linkTo(methodOn(UserController.class).getCustomerById(customer.getId())).withSelfRel());

                    ActionModel updateAction = new ActionModel("update", "POST",
                            linkTo(methodOn(UserController.class).updateCustomer(customer.getId(), null)).withSelfRel());
                    ActionModel deleteAction = new ActionModel("delete", "DELETE",
                            linkTo(methodOn(UserController.class).deleteCustomer(customer.getId())).withSelfRel());

                    customerModel.add(
                            linkTo(methodOn(UserController.class).getAllCustomers()).withRel("customers"),
                            updateAction.getLink().withRel(updateAction.getName()).withType(updateAction.getMethod()),
                            deleteAction.getLink().withRel(deleteAction.getName()).withType(deleteAction.getMethod()),
                            linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"),
                            linkTo(methodOn(TourController.class).getAllTours()).withRel("tours")
                    );

                    return customerModel;
                })
                .collect(Collectors.toList());

        return CollectionModel.of(customers,
                linkTo(methodOn(UserController.class).getAllCustomers()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<UserDto> getCustomerById(@PathVariable Long id) {
        UserDto customer = userService.getUserById(id);

        ActionModel updateAction = new ActionModel("update", "POST",
                linkTo(methodOn(UserController.class).updateCustomer(id, null)).withSelfRel());
        ActionModel deleteAction = new ActionModel("delete", "DELETE",
                linkTo(methodOn(UserController.class).deleteCustomer(id)).withSelfRel());

        return EntityModel.of(customer,
                linkTo(methodOn(UserController.class).getCustomerById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllCustomers()).withRel("customers"),
                updateAction.getLink().withRel(updateAction.getName()).withType(updateAction.getMethod()),
                deleteAction.getLink().withRel(deleteAction.getName()).withType(deleteAction.getMethod()),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"));
    }

    @PostMapping
    public EntityModel<UserDto> createCustomer(@RequestBody UserRequest customer) {
        UserDto createdCustomer = userService.createCustomer(customer);
        return EntityModel.of(createdCustomer,
                linkTo(methodOn(UserController.class).getCustomerById(createdCustomer.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllCustomers()).withRel("customers"));
    }

    @PostMapping("/{id}")
    public EntityModel<UserDto> updateCustomer(@PathVariable Long id, @RequestBody UserDto updatedCustomer) {
        UserDto customer = userService.updateCustomer(id, updatedCustomer);
        return EntityModel.of(customer,
                linkTo(methodOn(UserController.class).getCustomerById(customer.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllCustomers()).withRel("customers"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        userService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
