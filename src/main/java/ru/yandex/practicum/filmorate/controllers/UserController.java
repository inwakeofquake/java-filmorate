package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class UserController {

    private final Set<User> users = new HashSet<>();

    @GetMapping("/users")
    public List<User> getAll() {
        log.info("Retrieving all users.");
        return new ArrayList<>(users);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<User> add(@Valid @RequestBody User user) {
        log.info("Creating user with email {}", user.getEmail());

        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();

        for (User u : users) {
            if (u.getEmail().equals(email)) {
                log.error("User with email {} already exists.", email);
                throw new UserAlreadyExistsException();
            }
        }

        User newUser = new User(user.getId(), email, login, user.getName(), birthday);
        users.add(newUser);
        log.info("User with email {} has been added.", email);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Updating user with email {}", user.getEmail());

        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();

        for (User u : users) {
            if (u.getEmail().equals(email)) {
                u.setName(user.getName());
                u.setLogin(login);
                u.setBirthday(birthday);
                log.info("User with email {} has been updated.", email);
                return new ResponseEntity<>(u, HttpStatus.OK);
            }
        }

        log.error("User with email {} not found.", email);
        throw new UserNotFoundException();
    }

    @ExceptionHandler({ InvalidEmailException.class, InvalidUsernameException.class,
            InvalidBirthdayException.class, UserAlreadyExistsException.class, UserNotFoundException.class })
    public ResponseEntity<String> handleExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
