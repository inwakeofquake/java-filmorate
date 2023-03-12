package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class ValidateService {
    public void validateUser(User user){
        if(user.getId() < 0) {
            throw new RuntimeException("Invalid user ID: must be not negative");
        }
        if(user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            throw new RuntimeException("Invalid user login: null or blank");
        }
        if(!user.getEmail().matches("^[^\\s]+$") || !user.getEmail().contains("@")) {
            throw new RuntimeException("Invalid user email format");
        }

        if(user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new RuntimeException("Invalid user birthday: null or in the future");
        }
    }

    public void validateFilm(Film film){
        LocalDate firstFilm = LocalDate.of(1895,12,28);
        if(film.getId() < 0) {
            throw new RuntimeException("Invalid film ID: must be not negative");
        }
        if(film.getName() == null || film.getName().isBlank() || film.getName().isEmpty()) {
            throw new RuntimeException("Film name is null, blank or empty");
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
