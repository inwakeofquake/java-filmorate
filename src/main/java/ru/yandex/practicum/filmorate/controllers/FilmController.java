package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class FilmController {

    private final Set<Film> films = new HashSet<>();

    @GetMapping("/films")
    public List<Film> getAll() {
        log.info("Retrieving all films.");
        return new ArrayList<>(films);
    }

    @PostMapping(value = "/films")
    public ResponseEntity<Film> add(@Valid @RequestBody Film film) {
        log.info("Adding film {}", film.getName());

        Film newFilm = new Film(film.getId(), film.getName(),
                film.getDescription(), film.getReleaseDate(), film.getDuration());

        films.add(newFilm);

        log.info("Film {} has been added.", newFilm.getName());
        return new ResponseEntity<>(newFilm, HttpStatus.OK);
    }

    @PutMapping("/films/{id}")
    public ResponseEntity<Film> update(@PathVariable int id, @Valid @RequestBody Film updatedFilm) {
        for (Film film : films) {
            if (film.getId() == id) {
                film.setName(updatedFilm.getName());
                film.setDescription(updatedFilm.getDescription());
                film.setReleaseDate(updatedFilm.getReleaseDate());
                film.setDuration(updatedFilm.getDuration());

                log.info("Film {} updated: {}", id, film);

                return new ResponseEntity<>(film, HttpStatus.OK);
            }
        }

        log.error("Film not found: {}", id);

        throw new FilmNotFoundException();
    }
}

