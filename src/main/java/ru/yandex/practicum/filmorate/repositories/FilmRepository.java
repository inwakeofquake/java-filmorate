package ru.yandex.practicum.filmorate.repositories;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FilmRepository {
    private final Map<Long, Film> films = new ConcurrentHashMap<>();
    private long generatorId = 0;

    public long getGeneratedId() {
        return ++generatorId;
    }

    public void save(Film film) {
        film.setId(getGeneratedId());
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new RuntimeException("Film must have a name");
        }
        films.put(film.getId(),film);
    }

    public void update(Film film) {
        if(findById(film.getId()) == null) {
            throw new RuntimeException("No film with such id");
        }
        films.put(film.getId(),film);
    }

    public Film findById(Long id) {
        return films.get(id);
    }

    public ArrayList<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}
