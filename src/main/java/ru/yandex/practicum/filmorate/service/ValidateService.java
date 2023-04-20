package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@Component
public class ValidateService {
    private final LocalDate firstFilm = LocalDate.of(1895, 12, 28);

    public void validateUser(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new InvalidInputException("Invalid user login: null or empty");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidInputException("Invalid user birthday: null or in the future");
        }
        if (user.getFriendIds() == null) {
            throw new InvalidInputException("Invalid set of friends IDs");
        }
        if (user.getId() < 0) {
            throw new InvalidInputException("Invalid user ID: must not be negative");
        }
    }

    public void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Validation error: name for film {} is null, blank or empty", film.getName());
            throw new InvalidInputException("Invalid film ID: null, blank or empty");
        }
        if (film.getId() < 0) {
            log.warn("Validation error: film {} ID is negative", film.getName());
            throw new InvalidInputException("Invalid film ID: must not be negative");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.warn("Validation error: description for film {} is null or too long", film.getName());
            throw new InvalidInputException("Invalid film description: null or larger than 200 characters");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(firstFilm)) {
            log.warn("Validation error: film {} release date is null or before first film ever released", film.getName());
            throw new InvalidInputException("Invalid film release date: null or earlier than first film ever released");
        }
        if (film.getDuration() < 0) {
            log.warn("Validation error: film {} duration is negative", film.getName());
            throw new InvalidInputException("Invalid film duration: must not be negative");
        }
    }
}
