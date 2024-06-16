package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.entity.User;
import org.example.entity.dto.UserDTO;
import org.example.entity.dto.UserLoginDTO;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User addUser(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginDTO login(@RequestBody UserDTO userDTO) {
        return userService.checkCredentials(userDTO);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}
