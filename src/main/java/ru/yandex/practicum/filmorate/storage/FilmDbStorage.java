package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorageInterface {
    private final JdbcTemplate jdbcTemplate;
    private long generatorId = 0;

    private List<Genre> genres;
    private List<Mpa> mpas;

    private final RowMapper<Genre> filmGenresRowMapper = (resultSet, rowNum) ->
            getGenreById(resultSet.getLong("genre_id"));

    private final RowMapper<Long> filmLikesRowMapper = (resultSet, rowNum) ->
            resultSet.getLong("user_id");

    @Override
    public Genre getGenreById(long id) {
        for (Genre genre : genres) {
            if (genre.getId() == id)
                return genre;
        }
        return null;
    }

    private final RowMapper<Film> filmRowMapper = (resultSet, rowNum) -> {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString(("description")));
        film.setDuration(resultSet.getInt("duration"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setGenres(getGenresByFilmId(film.getId()));
        film.setMpa(getMpaById(resultSet.getLong("mpa_id")));
        return film;
    };

    private final RowMapper<Genre> genreRowMapper = (resultSet, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    };
    private final RowMapper<Mpa> mpaRowMapper = (resultSet, rowNum) -> {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("id"));
        mpa.setName(resultSet.getString("name"));
        return mpa;
    };

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long getGeneratedId() {
        return ++generatorId;
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
        deleteAllGenresForFilm(film);

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

    private void deleteAllGenresForFilm(Film film) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
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
    public boolean hasId(long id) {
        String sql = "SELECT COUNT(*) FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    @Override
    public List<Genre> getAllGenres() {
        if (genres == null) {
            fillGenres();
        }
        return genres;
    }

    @Override
    public boolean hasGenreById(long id) {
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

    @Override
    public List<Mpa> getAllMpas() {
        if (mpas == null) {
            fillMpas();
        }
        return mpas;
    }

    @Override
    public boolean hasMpaId(Long id) {
        if (mpas == null) {
            fillMpas();
        }
        for (Mpa mpa : mpas) {
            if (mpa.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void fillGenres() {
        String sql = "SELECT * FROM genres ORDER BY id ASC";
        genres = jdbcTemplate.query(sql, genreRowMapper);
    }

    private void fillMpas() {
        String sql = "SELECT * FROM mpas";
        mpas = jdbcTemplate.query(sql, mpaRowMapper);
    }

    private List<Genre> getGenresByFilmId(long id) {
        if (genres == null) {
            fillGenres();
        }
        String sql = "SELECT * FROM film_genres WHERE film_id = ?";
        return jdbcTemplate.query(sql, filmGenresRowMapper, id);
    }

    @Override
    public Mpa getMpaById(Long id) {
        if (mpas == null) {
            fillMpas();
        }
        for (Mpa mpa : mpas) {
            if (mpa.getId() == id)
                return mpa;
        }
        return null;
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