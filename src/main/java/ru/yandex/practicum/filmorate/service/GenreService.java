package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorageInterface;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GenreService {

    private final GenreDbStorageInterface genreDbStorage;

    public LinkedHashSet<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public boolean hasGenreId(Long id) {
        return genreDbStorage.hasGenreId(id);
    }

    public Genre getGenreId(Long id) {
        if (!hasGenreId(id)) {
            log.warn("Failed to retrieve film due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        return genreDbStorage.getGenreById(id);
    }
}
