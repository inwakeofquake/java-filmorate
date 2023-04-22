package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorageInterface {

    User save(User user);

    User update(User user);

    User getById(Long id);

    ArrayList<User> getAll();

    boolean hasId(long id);
}
