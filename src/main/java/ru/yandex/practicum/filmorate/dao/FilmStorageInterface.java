package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorageInterface {

    Film save(Film film);

    Film update(Film film);

    Film getById(Long id);

    ArrayList<Film> getAll();

    boolean hasId(Long id);

}
