package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final ValidateService validateService;
    private final InMemoryUserStorage repository;
    private final UserService userService;

    @GetMapping()
    public List<User> getAll() {
        log.info("Retrieving all users.");
        return repository.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = repository.getById(id);
        if (user == null)
            throw new NoSuchIdException("No such ID");
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        log.info("Saving user: {} - Started", user);
        validateService.validateUser(user);
        repository.save(user);
        log.info("Saving user: {} - Finished", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping()
    User updateUser(@Valid @RequestBody User user) {
        log.info("Updating user: {} - Started", user);
        validateService.validateUser(user);
        repository.update(user);
        log.info("Saving updated user: {}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        if (!repository.hasId(id) || !repository.hasId(friendId)) {
            throw new NoSuchIdException("No such ID");
        }
        userService.addFriendship(repository.getById(id), repository.getById(friendId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        if (!repository.hasId(id) || !repository.hasId(friendId)) {
            throw new NoSuchIdException("No such ID");
        }
        userService.removeFriendship(repository.getById(id), repository.getById(friendId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        if (!repository.hasId(id) || !repository.hasId(otherId)) {
            throw new NoSuchIdException("No such ID");
        }
        return userService.getMutualFriends(repository.getById(id), repository.getById(otherId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        if (!repository.hasId(id)) {
            throw new NoSuchIdException("No such ID");
        }
        User user = repository.getById(id);
        return userService.getFriends(user);
    }
}

