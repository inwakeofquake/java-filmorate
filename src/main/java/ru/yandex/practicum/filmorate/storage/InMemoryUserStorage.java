package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserStorage implements UserStorageInterface {
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private long generatorId = 0;

    public long getGeneratedId() {
        return ++generatorId;
    }

    public void save(User user) {
        user.setId(getGeneratedId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(),user);
    }

    public void update(User user) {
        if(getById(user.getId()) == null) {
            throw new NoSuchIdException("No such user ID");
        }
        for(long id:users.get(user.getId()).getFriendIds())
            user.addFriendId(id);
        users.put(user.getId(),user);
    }

    public ArrayList<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean hasId(long id) {
        return users.containsKey(id);
    }

    public User getById(Long id) {
        if(users.containsKey(id)) {
            return users.get(id);
        }
        return null;
    }
}