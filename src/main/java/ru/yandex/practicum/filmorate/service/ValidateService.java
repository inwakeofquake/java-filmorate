package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class ValidateService {
    private final LocalDate firstFilm = LocalDate.of(1895,12,28);

    public void validateUser(User user){
        if(user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new RuntimeException("Invalid user login: null or empty");
        }
        if(user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new RuntimeException("Invalid user birthday: null or in the future");
        }
        if(user.getId() < 0) {
            throw new RuntimeException("Invalid user ID: must not be negative");
        }
    }

    public void validateFilm(Film film){
        if(film.getName() == null || film.getName().isBlank() || film.getName().isEmpty()) {
            throw new RuntimeException("Film name is null, blank or empty");
        }
        if(film.getId() < 0) {
            throw new RuntimeException("Invalid film ID: must not be negative");
        }
        if(film.getDescription().length() > 200) {
            throw new RuntimeException("Film description is larger than 200 characters");
        }
        if(film.getReleaseDate().isBefore(firstFilm)) {
            throw new RuntimeException("Film release is earlier than first film ever released");
        }
        if(film.getDuration() < 0) {
            throw new RuntimeException("Film duration is negative");
        }
    }
}
