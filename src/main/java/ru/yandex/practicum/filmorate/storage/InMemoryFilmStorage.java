package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryFilmStorage implements FilmStorageInterface {
    private final Map<Long, Film> films = new ConcurrentHashMap<>();
    private long generatorId = 0;

    public long getGeneratedId() {
        return ++generatorId;
    }

    public void save(Film film) {
        film.setId(getGeneratedId());
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new InvalidInputException("Film name cannot be null or empty");
        }
        films.put(film.getId(),film);
    }

    public void update(Film film) {
        if(getById(film.getId()) == null) {
            throw new NoSuchIdException("No such film ID");
        }
        films.put(film.getId(),film);
    }

    public Film getById(Long id) {
        return films.get(id);
    }

    public ArrayList<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean hasId(long id) {
        return films.containsKey(id);
    }
}
