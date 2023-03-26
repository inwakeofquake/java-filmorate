package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorageInterface {
    long getGeneratedId();
    void save(Film film);
    void update(Film film);
    Film getById(Long id);
    ArrayList<Film> getAll();
    boolean hasId(long id);
}
