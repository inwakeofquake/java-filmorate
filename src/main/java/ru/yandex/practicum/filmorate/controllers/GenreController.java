package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("genres")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GenreController {

    private final FilmService filmService;

    @GetMapping()
    public List<Genre> getAll() {
        log.info("Retrieving all genres.");
        return filmService.getAllGenres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getById(@PathVariable Long id) {
        log.info("Retrieving genre by ID: {}", id);
        if (!filmService.hasGenreId(id)) {
            log.warn("Failed to retrieve film due to bad ID");
            throw new NoSuchIdException("No such ID");
        }

        return ResponseEntity.ok(filmService.getGenreId(id));
    }
}
