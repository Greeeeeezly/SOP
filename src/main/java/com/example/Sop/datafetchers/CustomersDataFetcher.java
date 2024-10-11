package com.example.Sop.datafetchers;

import com.example.Sop.dto.CustomerDto;
import com.example.Sop.services.CustomerService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class CustomersDataFetcher {

    private final CustomerService customerService;

    public CustomersDataFetcher(CustomerService customerService) {
        this.customerService = customerService;
    }

    @DgsQuery
    public List<CustomerDto> customers(@InputArgument String nameFilter) {
        List<CustomerDto> customers = customerService.getAllCustomers();
        if (nameFilter == null) {
            return customers;
        }

        return customers.stream()
                .filter(customer -> customer.getName().contains(nameFilter))
                .collect(Collectors.toList());
    }

    @DgsMutation
    public CustomerDto addCustomer(@InputArgument SubmittedCustomer customer) {
        CustomerDto newCustomer = new CustomerDto(customer.name(), customer.email(), customer.phoneNumber());
        newCustomer.setId(customerService.createCustomer(newCustomer).getId());
        return newCustomer;
    }

    @DgsMutation
    public CustomerDto updateCustomer(@InputArgument Long id, @InputArgument SubmittedCustomer customer) {
        CustomerDto existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer != null) {
            existingCustomer.setName(customer.name());
            existingCustomer.setEmail(customer.email());
            existingCustomer.setPhoneNumber(customer.phoneNumber());
            customerService.updateCustomer(id, existingCustomer);
            return existingCustomer;
        }
        return null;
    }

    @DgsMutation
    public void deleteCustomer(@InputArgument Long id) {
        customerService.deleteCustomer(id);
    }

    record SubmittedCustomer(Long id, String name, String email, String phoneNumber) {}
}
