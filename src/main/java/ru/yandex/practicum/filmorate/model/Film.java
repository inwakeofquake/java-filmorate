package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validator.ValidFilmReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private Long id;

    @NotNull(message = "Film name must not be null")
    @NotBlank(message = "Film name must not be blank")
    @NotEmpty(message = "Film name must not be empty")
    private String name;

    @Size(max = 200, message = "Film description must not exceed 200 characters")
    private String description;

    @ValidFilmReleaseDate(message = "Film release date must be between December 28, 1895 and the present")
    private LocalDate releaseDate;

    @Positive(message = "Film duration must be positive")
    private int duration;

    private LinkedHashSet<Genre> genres;

    @NotNull(message = "MPA must not be null")
    private Mpa mpa;
}
