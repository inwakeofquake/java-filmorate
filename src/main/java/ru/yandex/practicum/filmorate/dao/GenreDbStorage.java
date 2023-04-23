package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

@Component
public class GenreDbStorage implements GenreDbStorageInterface {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreRowMapper = (resultSet, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    };
    private LinkedHashSet<Genre> genres;
    private final RowMapper<Genre> filmGenresRowMapper = (resultSet, rowNum) ->
            getGenreById(resultSet.getLong("genre_id"));

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(long id) {
        for (Genre genre : genres) {
            if (genre.getId() == id)
                return genre;
        }
        return null;
    }

    @Override
    public void deleteAllGenresForFilm(Film film) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    @Override
    public LinkedHashSet<Genre> getAllGenres() {
        if (genres == null) {
            fillGenres();
        }
        return genres;
    }

    @Override
    public LinkedHashSet<Genre> getGenresByFilmId(long id) {
        if (genres == null) {
            fillGenres();
        }
        String sql = "SELECT * FROM film_genres WHERE film_id = ?";
        return new LinkedHashSet<>(jdbcTemplate.query(sql, filmGenresRowMapper, id));
    }

    @Override
    public boolean hasGenreId(Long id) {
        if (genres == null) {
            fillGenres();
        }
        for (Genre genre : genres) {
            if (genre.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void fillGenres() {
        String sql = "SELECT * FROM genres ORDER BY id ASC";
        genres = new LinkedHashSet<>(jdbcTemplate.query(sql, genreRowMapper));
    }
}
