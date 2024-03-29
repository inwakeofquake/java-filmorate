package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("genres")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GenreController {

    private final GenreService genreService;

    @GetMapping()
    public Set<Genre> getAll() {
        log.info("Retrieving all genres.");
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getById(@PathVariable Long id) {
        log.info("Retrieving genre by ID: {}", id);
        return ResponseEntity.ok(genreService.getGenreId(id));
    }
}
