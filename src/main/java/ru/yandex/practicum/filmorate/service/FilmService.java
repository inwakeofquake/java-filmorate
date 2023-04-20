package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorageInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilmService {

    private final FilmStorageInterface filmStorage;

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NoSuchIdException("No such film ID");
        }
        film.addLike(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NoSuchIdException("No such film ID");
        }
        film.removeLike(userId);
    }

    public List<Film> getTopFilmsByLikes(int count) {
        List<Film> allFilms = filmStorage.getAll();
        allFilms.sort(Comparator.comparingInt(film -> film.getLikes().size()));
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
        return filmStorage.getById(id);
    }

    public void save(Film film) {
        filmStorage.save(film);
    }

    public void update(Film film) {
        filmStorage.update(film);
    }
}

