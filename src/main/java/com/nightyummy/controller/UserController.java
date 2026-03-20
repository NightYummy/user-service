package com.nightyummy.controller;

import com.nightyummy.dto.UserDTO;
import com.nightyummy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody UserDTO user) {
        userService.createUser(user);
    }

    @GetMapping("/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PutMapping("/{email}")
    public UserDTO updateUser(@PathVariable String email, @Valid @RequestBody UserDTO request) {
        return userService.updateUser(email, request);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(userService.getUserByEmail(email));
    }
}
