package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilmController {

    private final ValidateService validateService;
    private final FilmService filmService;
    private final UserService userService;

    @GetMapping()
    public List<Film> getAll() {
        log.info("Retrieving all films.");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getById(@PathVariable Long id) {
        log.info("Retrieving film by ID: {}", id);
        if (!filmService.hasId(id)) {
            log.warn("Failed to retrieve film due to bad ID");
            throw new NoSuchIdException("No such ID");
        }

        return ResponseEntity.ok(filmService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Film> saveFilm(@RequestBody Film film) {
        log.info("Saving film {}", film.getName());
        try {
            validateService.validateFilm(film);
        } catch (ValidationException ex) {
            log.warn("Validation error occurred while saving the film {}", film.getName());
            return ResponseEntity.badRequest().build();
        }
        filmService.save(film);
        log.info("Film {} has been saved.", film.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping()
    Film updateFilm(@RequestBody Film film) {
        log.info("Updating film: {} - Started", film);
        validateService.validateFilm(film);
        filmService.update(film);
        log.info("Saving updated film: {}", film);
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("User {} liked film {}", userId, filmId);
        if (!filmService.hasId(filmId) || !userService.hasId(userId)) {
            log.warn("Failed to add like: user ID {} or film ID {} not found", userId, filmId);
            throw new NoSuchIdException("No such ID");
        }

        filmService.addLike(filmId, userId);
        return ResponseEntity.ok(filmService.getById(filmId));
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        if (!filmService.hasId(filmId) || !userService.hasId(userId)) {
            log.warn("Failed to remove like of user {} from film {}: bad ID", userId, filmId);
            throw new NoSuchIdException("No such ID");
        }
        log.info("User {} removed like from film {}", userId, filmId);
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilmsByLikes(@RequestParam(name = "count", defaultValue = "10") String count) {
        return filmService.getTopFilmsByLikes(Integer.parseInt(count));
    }
}

