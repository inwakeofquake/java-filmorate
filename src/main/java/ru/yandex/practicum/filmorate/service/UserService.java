package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorageInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorageInterface userStorage;

    public User saveUser(User user) {
        return userStorage.save(user);
    }

    public void addFriendship(User user1, User user2) {
        if (!hasId(user1.getId()) || !hasId(user2.getId())) {
            log.warn("Failed to add friendship due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        userStorage.addFriend(user1, user2);
    }

    public void removeFriendship(User user1, User user2) {
        if (!hasId(user1.getId()) || !hasId(user2.getId())) {
            log.warn("Failed to remove user from friends due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        userStorage.removeFriend(user1, user2);
    }

    public List<User> getFriends(User user) {
        if (!hasId(user.getId())) {
            log.warn("Failed to retrieve friends of user due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        return userStorage.getFriendsById(user.getId());
    }

    public List<User> getMutualFriends(User user1, User user2) {
        if (!hasId(user1.getId()) || !hasId(user2.getId())) {
            log.warn("Failed to retrieve mutual friends of users due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        Set<Long> mutual = new HashSet<>(userStorage.getFriendsIdsById(user1.getId()));
        mutual.retainAll(userStorage.getFriendsIdsById(user2.getId()));
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
        if (!hasId(id)) {
            log.warn("Failed to retrieve user with ID {}", id);
            throw new NoSuchIdException("No such ID");
        }
        return userStorage.getById(id);
    }

    public User update(User user) {
        if (!hasId(user.getId())) {
            log.warn("Failed to update user due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        return userStorage.update(user);
    }

    public boolean hasId(long id) {
        return userStorage.hasId(id);
    }
}