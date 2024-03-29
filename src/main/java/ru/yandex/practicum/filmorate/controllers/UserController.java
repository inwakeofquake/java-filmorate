package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> getAll() {
        log.info("Retrieving all users.");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Retrieving user with ID {}", id);
        User user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody @Valid User user) {
        log.info("Saving user: {} - Started", user);
        User savedUser = userService.saveUser(user);
        log.info("Saving user: {} - Finished", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping()
    User updateUser(@RequestBody @Valid User user) {
        log.info("Updating user: {} - Started", user);
        User savedUser = userService.update(user);
        log.info("Saving updated user: {}", savedUser);
        return savedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Adding user {} as friend to user {}", friendId, id);
        userService.addFriendship(userService.getById(id), userService.getById(friendId));
        log.info("User {} added as friend of user {}", friendId, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Removing user {} from friends of user {}", friendId, id);
        userService.removeFriendship(userService.getById(id), userService.getById(friendId));
        log.info("User {} removed from friends of user {}", friendId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Retrieving list of mutual friends of users {} and {}", id, otherId);
        return userService.getMutualFriends(userService.getById(id), userService.getById(otherId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Retrieving list of friends of user {}", id);
        User user = userService.getById(id);
        return userService.getFriends(user);
    }
}