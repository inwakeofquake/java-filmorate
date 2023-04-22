package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class FilmDbStorage implements FilmStorageInterface {
    private final JdbcTemplate jdbcTemplate;
    private GenreDbStorageInterface genreDbStorage;
    private MpaDbStorageInterface mpaDbStorage;

    private final RowMapper<Film> filmRowMapper = (resultSet, rowNum) -> {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString(("description")));
        film.setDuration(resultSet.getInt("duration"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
        film.setMpa(mpaDbStorage.getMpaById(resultSet.getLong("mpa_id")));
        return film;
    };

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorageInterface genreDbStorage, MpaDbStorageInterface mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public Film save(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new InvalidInputException("Film name cannot be null or empty");
        }
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId());
        sql = "SELECT * FROM films WHERE name = ? ORDER BY id DESC LIMIT 1";
        Film resultFilm = jdbcTemplate.queryForObject(sql, filmRowMapper, film.getName());
        if (film.getGenres() != null) {
            for (Genre genre : new HashSet<>(film.getGenres())) {
                addGenreToFilm(resultFilm, genre);
            }
        }
        resultFilm = jdbcTemplate.queryForObject(sql, filmRowMapper, film.getName());
        return resultFilm;
    }

    @Override
    public Film update(Film film) {
        if (!hasId(film.getId())) {
            throw new NoSuchIdException("No such film ID");
        }
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        genreDbStorage.deleteAllGenresForFilm(film);

        if (film.getGenres() != null) {
            for (Genre genre : new HashSet<>(film.getGenres())) {
                addGenreToFilm(film, genre);
            }
        }
        sql = "SELECT * FROM films WHERE id = ? ";
        return jdbcTemplate.queryForObject(sql, filmRowMapper, film.getId());
    }

    private void addGenreToFilm(Film film, Genre genre) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, film.getId(), genre.getId());
    }

    @Override
    public Film getById(Long id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, filmRowMapper, id);
    }

    @Override
    public ArrayList<Film> getAll() {
        String sql = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper);
        return new ArrayList<>(films);
    }

    @Override
    public boolean hasId(Long id) {
        String sql = "SELECT COUNT(*) FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}