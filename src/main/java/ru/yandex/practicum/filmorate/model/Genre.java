package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @NotNull
    private long id;
    private String name;
}
