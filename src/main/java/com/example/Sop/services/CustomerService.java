package com.example.Sop.services;

import com.example.Sop.dto.CustomerDto;
import com.example.Sop.models.Customer;
import com.example.Sop.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return modelMapper.map(customer, CustomerDto.class);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        Customer savedCustomer = customerRepository.save(customer);
        CustomerDto ds = modelMapper.map(savedCustomer, CustomerDto.class);
        return modelMapper.map(savedCustomer, CustomerDto.class);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto updatedCustomerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existingCustomer.setName(updatedCustomerDto.getName());
        existingCustomer.setEmail(updatedCustomerDto.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomerDto.getPhoneNumber());

        Customer updatedCustomer = customerRepository.save(existingCustomer);

        return modelMapper.map(updatedCustomer, CustomerDto.class);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }
}
