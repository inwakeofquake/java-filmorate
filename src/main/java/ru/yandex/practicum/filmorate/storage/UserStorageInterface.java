package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorageInterface {
    long getGeneratedId();

    void save(User user);

    void update(User user);

    User getById(Long id);

    ArrayList<User> getAll();

    boolean hasId(long id);
}
