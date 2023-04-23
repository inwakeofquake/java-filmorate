package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("mpa")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MpaController {
    private final MpaService mpaService;

    @GetMapping()
    public List<Mpa> getAll() {
        log.info("Retrieving all MPA ratings.");
        return mpaService.getAllMpas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getById(@PathVariable Long id) {
        log.info("Retrieving MPA rating by ID: {}", id);
        return ResponseEntity.ok(mpaService.getMpaId(id));
    }
}
