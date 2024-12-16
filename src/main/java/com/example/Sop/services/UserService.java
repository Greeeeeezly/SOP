package com.example.Sop.services;

import com.example.Sop.models.User;
import com.example.Sop.repositories.UserRepository;
import com.example.excursionbookingapi.dto.UserDto;
import com.example.excursionbookingapi.dto.UserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<UserDto> getAllUsers() {
        List<User> customers = userRepository.findAll();
        return customers.stream()
                .map(customer -> modelMapper.map(customer, UserDto.class))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto createCustomer(UserRequest userDto) {
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        UserDto mappedCustomer = modelMapper.map(savedUser, UserDto.class);
        return mappedCustomer;
    }


    public UserDto updateCustomer(Long id, UserDto updatedUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existingUser.setName(updatedUserDto.getName());
        existingUser.setEmail(updatedUserDto.getEmail());

        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    public void deleteCustomer(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        userRepository.delete(user);
    }
}
