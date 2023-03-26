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
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilmController {

    private final ValidateService validateService;
    private final FilmService filmService;
    private final InMemoryFilmStorage repository;
    private final InMemoryUserStorage userStorage;

    @GetMapping()
    public List<Film> getAll() {
        log.info("Retrieving all films.");
        return repository.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getById(@PathVariable Long id) {
        if (!repository.hasId(id)) {
            throw new NoSuchIdException("No such ID");
        }
        log.info("Retrieving film by id: {}", id);
        return ResponseEntity.ok(repository.getById(id));
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
        repository.save(film);
        log.info("Film {} has been saved.", film.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping()
    Film updateFilm(@RequestBody Film film) {
        log.info("Updating film: {} - Started", film);
        validateService.validateFilm(film);
        repository.update(film);
        log.info("Saving updated film: {}", film);
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        if (!repository.hasId(filmId) || !userStorage.hasId(userId)) {
            throw new NoSuchIdException("No such ID");
        }
        log.info("User {} liked film {}", userId, filmId);
        filmService.addLike(filmId, userId);
        return ResponseEntity.ok(repository.getById(filmId));
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        if (!repository.hasId(filmId) || !userStorage.hasId(userId)) {
            throw new NoSuchIdException("No such ID");
        }
        log.info("User {} removed like from film {}", userId, filmId);
        filmService.removeLike(filmId,userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilmsByLikes(@RequestParam(name = "count", defaultValue = "10") String count) {
        return filmService.getTopFilmsByLikes(Integer.parseInt(count));
    }
}

