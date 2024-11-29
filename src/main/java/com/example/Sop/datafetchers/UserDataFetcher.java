package com.example.Sop.datafetchers;

import com.example.Sop.dto.UserDto;
import com.example.Sop.services.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class UserDataFetcher {

    private final UserService userService;

    public UserDataFetcher(UserService userService) {
        this.userService = userService;
    }

    @DgsQuery
    public List<UserDto> users(@InputArgument String nameFilter) {
        List<UserDto> users = userService.getAllUsers();
        if (nameFilter == null) {
            return users;
        }

        return users.stream()
                .filter(user -> user.getName().contains(nameFilter))
                .collect(Collectors.toList());
    }

    @DgsMutation
    public UserDto addUser(@InputArgument(name = "user") SubmittedUser userInput) {
        UserDto newUser = new UserDto(userInput.name(), userInput.email(), userInput.priority());
        newUser.setId(userService.createCustomer(newUser).getId());
        return newUser;
    }

    @DgsMutation
    public UserDto updateUser(@InputArgument Long id, @InputArgument(name = "user") SubmittedUser userInput) {
        UserDto existingUser = userService.getUserById(id);
        if (existingUser != null) {
            existingUser.setName(userInput.name());
            existingUser.setEmail(userInput.email());
            existingUser.setPriority(userInput.priority());
            userService.updateCustomer(id, existingUser);
            return existingUser;
        }
        return null;
    }

    @DgsMutation
    public void deleteUser(@InputArgument Long id) {
        userService.deleteCustomer(id);
    }

    public record SubmittedUser(String name, String email, Boolean priority) {}
}
