package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserStorageInterface {
    long getGeneratedId();

    User save(User user);

    User update(User user);

    User getById(Long id);

    ArrayList<User> getAll();

    boolean hasId(long id);

    List<User> getFriendsById(long id);

    List<Long> getFriendsIdsById(long id);

    void addFriend(User user, User friend);

    void removeFriend(User user, User exFriend);
}
