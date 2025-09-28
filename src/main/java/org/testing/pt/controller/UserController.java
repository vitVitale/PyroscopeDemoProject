package org.testing.pt.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.testing.pt.dto.UserDto;
import org.testing.pt.model.User;
import org.testing.pt.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@Valid @RequestBody UserDto userDto) {
        User user = new User(null, userDto.getName(), userDto.getEmail());
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + id)));
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }
}