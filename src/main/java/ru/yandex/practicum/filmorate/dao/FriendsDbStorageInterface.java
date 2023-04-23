package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDbStorageInterface {
    List<User> getFriendsById(long id);

    List<Long> getFriendsIdsById(long id);

    void addFriend(User user, User friend);

    void removeFriend(User user, User exFriend);
}
