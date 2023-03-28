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

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final ValidateService validateService;
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
        if (user == null) {
            log.warn("Failed to retrieve user with ID {}", id);
            throw new NoSuchIdException("No such ID");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        log.info("Saving user: {} - Started", user);
        validateService.validateUser(user);
        userService.saveUser(user);
        log.info("Saving user: {} - Finished", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping()
    User updateUser(@Valid @RequestBody User user) {
        log.info("Updating user: {} - Started", user);
        validateService.validateUser(user);
        userService.update(user);
        log.info("Saving updated user: {}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Adding user {} as friend to user {}", friendId, id);
        if (!userService.hasId(id) || !userService.hasId(friendId)) {
            log.warn("Failed to add friendship due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        userService.addFriendship(userService.getById(id), userService.getById(friendId));
        log.info("User {} added as friend of user {}", friendId, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Removing user {} from friends of user {}", friendId, id);
        if (!userService.hasId(id) || !userService.hasId(friendId)) {
            log.warn("Failed to remove user from friends due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        userService.removeFriendship(userService.getById(id), userService.getById(friendId));
        log.info("User {} removed from friends of user {}", friendId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Retrieving list of mutual friends of users {} and {}", id, otherId);
        if (!userService.hasId(id) || !userService.hasId(otherId)) {
            log.warn("Failed to retrieve mutual friends of users due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        return userService.getMutualFriends(userService.getById(id), userService.getById(otherId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Retrieving list of friends of user {}", id);
        if (!userService.hasId(id)) {
            log.warn("Failed to retrieve friends of user due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        User user = userService.getById(id);
        return userService.getFriends(user);
    }
}