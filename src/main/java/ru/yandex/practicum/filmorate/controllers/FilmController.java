package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repositories.FilmRepository;
import ru.yandex.practicum.filmorate.service.ValidateService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final ValidateService validateService;

    @Autowired
    private FilmRepository repository;

    public FilmController(ValidateService validateService) {
        this.validateService = validateService;
    }

    @GetMapping()
    public List<Film> getAll() {
        log.info("Retrieving all films.");
        return repository.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Film saveFilm(@RequestBody Film film) {
        log.info("Saving film {}", film.getName());
        validateService.validateFilm(film);
        repository.save(film);
        log.info("Film {} has been saved.", film.getName());
        return film;
    }

    @PutMapping()
    Film updateFilm(@RequestBody Film film) {
        log.info("Updating film: {} - Started", film);
        validateService.validateFilm(film);
        repository.update(film);
        log.info("Saving updated film: {}", film);
        return film;
    }
}

