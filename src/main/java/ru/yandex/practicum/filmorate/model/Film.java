package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validators.PositiveNotNullDuration;
import ru.yandex.practicum.filmorate.validators.ReleaseDateConstraint;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @NotNull
    private int id;

    @NotNull
    @NotBlank(message = "Film name cannot be blank")
    private String name;

    @Size(max = 200)
    private String description;

    @NotNull
    @ReleaseDateConstraint(message = "Release date cannot be earlier than December 28, 1895")
    private LocalDate releaseDate;

    @PositiveNotNullDuration
    private Duration duration;
}
