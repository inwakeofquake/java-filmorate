package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorageInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorageInterface filmStorage;

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (!hasId(filmId) || !hasId(userId)) {
            log.warn("Failed to add like: user ID {} or film ID {} not found", userId, filmId);
            throw new NoSuchIdException("No such ID");
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (!hasId(filmId) || !hasId(userId)) {
            log.warn("Failed to remove like of user {} from film {}: bad ID", userId, filmId);
            throw new NoSuchIdException("No such ID");
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getTopFilmsByLikes(int count) {
        List<Film> allFilms = filmStorage.getAll();
        allFilms.sort(Comparator.comparingInt(film -> filmStorage.getLikes(film.getId()).size()));
        Collections.reverse(allFilms);
        return allFilms.subList(0, Math.min(allFilms.size(), count));
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public boolean hasId(Long id) {
        return filmStorage.hasId(id);
    }

    public Film getById(Long id) {
        if (!hasId(id)) {
            log.warn("Failed to retrieve film due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        return filmStorage.getById(id);
    }

    public Film save(Film film) {
        if (film == null) {
            log.warn("Failed to save film due to null");
            throw new InvalidInputException("Invalid input");
        }
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public boolean hasGenreId(Long id) {
        return filmStorage.hasGenreById(id);
    }

    public Genre getGenreId(Long id) {
        return filmStorage.getGenreById(id);
    }

    public List<Mpa> getAllMpas() {
        return filmStorage.getAllMpas();
    }

    public boolean hasMpaId(Long id) {
        return filmStorage.hasMpaId(id);
    }

    public Mpa getMpaId(Long id) {
        return filmStorage.getMpaById(id);
    }
}