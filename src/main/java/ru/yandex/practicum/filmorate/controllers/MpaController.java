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
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("mpa")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MpaController {
    private final FilmService filmService;

    @GetMapping()
    public List<Mpa> getAll() {
        log.info("Retrieving all MPA ratings.");
        return filmService.getAllMpas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getById(@PathVariable Long id) {
        log.info("Retrieving MPA rating by ID: {}", id);
        if (!filmService.hasMpaId(id)) {
            log.warn("Failed to retrieve MPA rating due to bad ID");
            throw new NoSuchIdException("No such ID");
        }

        return ResponseEntity.ok(filmService.getMpaId(id));
    }
}
