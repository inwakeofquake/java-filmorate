package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

public interface FilmStorageInterface {
    long getGeneratedId();

    Film save(Film film);

    Film update(Film film);

    Film getById(Long id);

    ArrayList<Film> getAll();

    boolean hasId(long id);

    List<Genre> getAllGenres();

    Genre getGenreById(long id);

    boolean hasGenreById(long id);

    List<Mpa> getAllMpas();

    boolean hasMpaId(Long id);

    Mpa getMpaById(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Long> getLikes(Long filmId);
}
