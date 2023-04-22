package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikesDbStorage implements LikesDbStorageInterface {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Long> filmLikesRowMapper = (resultSet, rowNum) ->
            resultSet.getLong("user_id");

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Long> getLikes(Long filmId) {
        String sql = "SELECT * FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, filmLikesRowMapper, filmId);
    }

}
