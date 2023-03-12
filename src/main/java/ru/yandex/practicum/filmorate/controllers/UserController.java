package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repositories.UserRepository;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    final ValidateService validateService;

    @Autowired
    UserRepository repository;

    public UserController(ValidateService validateService) {
        this.validateService = new ValidateService();
    }

    @GetMapping()
    public List<User> getAll() {
        log.info("Retrieving all users.");
        return repository.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    User saveUser(@RequestBody User user) {
        log.info("Saving user: {} - Started", user);
        validateService.validateUser(user);
        repository.save(user);
        log.info("Saving user: {} - Finished", user);
        return user;
    }

    @PutMapping()
    User updateUser(@RequestBody User user) {
        log.info("Updating user: {} - Started", user);
        validateService.validateUser(user);
        repository.update(user);
        log.info("Saving updated user: {}", user);
        return user;
    }
}
