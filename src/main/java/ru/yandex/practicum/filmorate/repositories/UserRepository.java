package ru.yandex.practicum.filmorate.repositories;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private long generatorId = 0;

    public long getGeneratedId() {
        return ++generatorId;
    }

    public void save(User user) {
        user.setId(getGeneratedId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(),user);
    }

    public void update(User user) {
        if(findById(user.getId()) == null) {
            throw new RuntimeException("No user with such id");
        }
        users.put(user.getId(),user);
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public ArrayList<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
