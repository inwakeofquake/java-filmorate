package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDbStorageInterface {
    List<Mpa> getAllMpas();

    boolean hasMpaId(Long id);

    Mpa getMpaById(Long id);
}
