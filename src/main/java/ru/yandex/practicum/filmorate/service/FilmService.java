package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorageInterface;
import ru.yandex.practicum.filmorate.dao.GenreDbStorageInterface;
import ru.yandex.practicum.filmorate.dao.LikesDbStorageInterface;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilmService {

    private final FilmStorageInterface filmStorage;
    private final GenreDbStorageInterface genreDbStorage;
    private final LikesDbStorageInterface likesDbStorage;

    public void addLike(Long filmId, Long userId) {
        if (!hasId(filmId) || !hasId(userId)) {
            log.warn("Failed to add like: user ID {} or film ID {} not found", userId, filmId);
            throw new NoSuchIdException("No such ID");
        }
        likesDbStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (!hasId(filmId) || !hasId(userId)) {
            log.warn("Failed to remove like of user {} from film {}: bad ID", userId, filmId);
            throw new NoSuchIdException("No such ID");
        }
        likesDbStorage.removeLike(filmId, userId);
    }

    public List<Film> getTopFilmsByLikes(int count) {
        List<Film> allFilms = filmStorage.getAll();
        allFilms.sort(Comparator.comparingInt(film -> likesDbStorage.getLikes(film.getId()).size()));
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

    public boolean hasGenreId(Long id) {
        return genreDbStorage.hasGenreId(id);
    }

}