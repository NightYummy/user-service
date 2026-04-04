package com.nightyummy.controller;

import com.nightyummy.dto.UserDTO;
import com.nightyummy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Users", description = "Users management")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Creates a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "User exists")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserDTO> createUser(@Valid @RequestBody UserDTO dto) {
        UserDTO newUser = userService.createUser(dto);
        return EntityModel.of(newUser,
                linkTo(methodOn(UserController.class).getUserByEmail(newUser.getEmail())).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(newUser.getEmail(), null)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(newUser.getEmail())).withRel("delete")
        );
    }

    @Operation(summary = "Returns a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    @GetMapping("/{email}")
    public EntityModel<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserByEmail(email)).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(email, null)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(email)).withRel("delete"),
                linkTo(methodOn(UserController.class).createUser(null)).withRel("create")
        );
    }

    @Operation(summary = "Updates a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "User exists")
    })
    @PutMapping("/{email}")
    public EntityModel<UserDTO> updateUser(@PathVariable String email, @Valid @RequestBody UserDTO dto) {
        UserDTO updated = userService.updateUser(email, dto);
        return EntityModel.of(updated,
                linkTo(methodOn(UserController.class).getUserByEmail(updated.getEmail())).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(updated.getEmail(), null)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(updated.getEmail())).withRel("delete")
        );
    }

    @Operation(summary = "Deletes a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        Link link = linkTo(methodOn(UserController.class).createUser(null)).withRel("create");
        return ResponseEntity.noContent().header("Link", link.toString()).build();
    }
}
