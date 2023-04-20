package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorageInterface {
    private final JdbcTemplate jdbcTemplate;
    private long generatorId;

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    };

    private final RowMapper<User> userFriendsRowMapper = (resultSet, rowNum) -> {
        return getById(resultSet.getLong("friend_id"));
    };

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long getGeneratedId() {
        return ++generatorId;
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (login, name, email, birthday) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        sql = "SELECT * FROM users WHERE login = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, user.getLogin());
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        sql = "SELECT * FROM users WHERE login = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, user.getLogin());
    }

    @Override
    public User getById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    @Override
    public ArrayList<User> getAll() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, userRowMapper);
        return new ArrayList<>(users);
    }

    @Override
    public boolean hasId(long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    @Override
    public List<User> getFriendsById(long id) {
        String sql = "SELECT * FROM user_friends WHERE user_id = ?";
        return jdbcTemplate.query(sql, userFriendsRowMapper, id);
    }

    @Override
    public List<Long> getFriendsIdsById(long id) {
        List<Long> result = new ArrayList<>();
        for (User user : getFriendsById(id)) {
            result.add(user.getId());
        }
        return result;
    }

    @Override
    public void addFriend(User user, User friend) {
        String sql = "INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getId(), friend.getId());
    }

    @Override
    public void removeFriend(User user, User exFriend) {
        String sql = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, user.getId(), exFriend.getId());
    }
}

