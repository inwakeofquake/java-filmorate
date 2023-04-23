package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDbStorageInterface;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MpaService {

    private final MpaDbStorageInterface mpaDbStorage;

    public List<Mpa> getAllMpas() {
        return mpaDbStorage.getAllMpas();
    }

    public boolean hasMpaId(Long id) {
        return mpaDbStorage.hasMpaId(id);
    }

    public Mpa getMpaId(Long id) {
        if (!hasMpaId(id)) {
            log.warn("Failed to retrieve MPA rating due to bad ID");
            throw new NoSuchIdException("No such ID");
        }
        return mpaDbStorage.getMpaById(id);
    }
}
