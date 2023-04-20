package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorageInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private final UserStorageInterface userStorage;

    public void saveUser(User user) {
        userStorage.save(user);
    }

    public void addFriendship(User user1, User user2) {
        user1.addFriendId(user2.getId());
        user2.addFriendId(user1.getId());
    }

    public void removeFriendship(User user1, User user2) {
        user1.removeFriendId(user2.getId());
        user2.removeFriendId(user1.getId());
    }

    public List<User> getFriends(User user) {
        List<User> friendsList = new ArrayList<>();
        for (Long friendId : user.getFriendIds()) {
            User friend = userStorage.getById(friendId);
            if (friend != null) {
                friendsList.add(friend);
            } else {
                throw new NoSuchIdException("No such ID");
            }
        }
        return friendsList;
    }

    public List<User> getMutualFriends(User user1, User user2) {
        Set<Long> mutual = new HashSet<>(user1.getFriendIds());
        mutual.retainAll(user2.getFriendIds());
        List<User> friendsList = new ArrayList<>();
        for (Long friendId : mutual) {
            User friend = userStorage.getById(friendId);
            if (friend != null) {
                friendsList.add(friend);
            } else {
                throw new NoSuchIdException("No such ID");
            }
        }
        return friendsList;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public void update(User user) {
        userStorage.update(user);
    }

    public boolean hasId(long id) {
        return userStorage.hasId(id);
    }
}