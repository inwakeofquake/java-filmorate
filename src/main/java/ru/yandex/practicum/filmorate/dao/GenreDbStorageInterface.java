package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

public interface GenreDbStorageInterface {
    void deleteAllGenresForFilm(Film film);

    LinkedHashSet<Genre> getAllGenres();

    Genre getGenreById(long id);

    LinkedHashSet<Genre> getGenresByFilmId(long id);

    boolean hasGenreId(Long id);
}
