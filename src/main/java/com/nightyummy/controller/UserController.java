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
    public UserDTO createUser(@Valid @RequestBody UserDTO dto) {
        try {
            return userService.createUser(dto);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        try {
            return userService.getUserByEmail(email);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PutMapping("/{email}")
    public UserDTO updateUser(@PathVariable String email, @Valid @RequestBody UserDTO dto) {
        try {
            return userService.updateUser(email, dto);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return userService.getUserByEmail(email);
        }
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
