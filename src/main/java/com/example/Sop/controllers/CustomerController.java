package com.example.Sop.controllers;

import com.example.Sop.ActionModel;
import com.example.Sop.dto.CustomerDto;
import com.example.Sop.models.Customer;
import com.example.Sop.services.CustomerService;
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
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public CollectionModel<EntityModel<CustomerDto>> getAllCustomers() {
        List<EntityModel<CustomerDto>> customers = customerService.getAllCustomers().stream()
                .map(customer -> {
                    EntityModel<CustomerDto> customerModel = EntityModel.of(customer,
                            linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel());

                    ActionModel updateAction = new ActionModel("update", "POST",
                            linkTo(methodOn(CustomerController.class).updateCustomer(customer.getId(), null)).withSelfRel());
                    ActionModel deleteAction = new ActionModel("delete", "DELETE",
                            linkTo(methodOn(CustomerController.class).deleteCustomer(customer.getId())).withSelfRel());

                    customerModel.add(
                            linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"),
                            updateAction.getLink().withRel(updateAction.getName()).withType(updateAction.getMethod()),
                            deleteAction.getLink().withRel(deleteAction.getName()).withType(deleteAction.getMethod()),
                            linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"),
                            linkTo(methodOn(TourController.class).getAllTours()).withRel("tours")
                    );

                    return customerModel;
                })
                .collect(Collectors.toList());

        return CollectionModel.of(customers,
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CustomerDto> getCustomerById(@PathVariable Long id) {
        CustomerDto customer = customerService.getCustomerById(id);

        ActionModel updateAction = new ActionModel("update", "POST",
                linkTo(methodOn(CustomerController.class).updateCustomer(id, null)).withSelfRel());
        ActionModel deleteAction = new ActionModel("delete", "DELETE",
                linkTo(methodOn(CustomerController.class).deleteCustomer(id)).withSelfRel());

        return EntityModel.of(customer,
                linkTo(methodOn(CustomerController.class).getCustomerById(id)).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"),
                updateAction.getLink().withRel(updateAction.getName()).withType(updateAction.getMethod()),
                deleteAction.getLink().withRel(deleteAction.getName()).withType(deleteAction.getMethod()),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"));
    }

    @PostMapping
    public EntityModel<CustomerDto> createCustomer(@RequestBody CustomerDto customer) {
        CustomerDto createdCustomer = customerService.createCustomer(customer);
        return EntityModel.of(createdCustomer,
                linkTo(methodOn(CustomerController.class).getCustomerById(createdCustomer.getId())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"));
    }

    @PostMapping("/{id}")
    public EntityModel<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto updatedCustomer) {
        CustomerDto customer = customerService.updateCustomer(id, updatedCustomer);
        return EntityModel.of(customer,
                linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
