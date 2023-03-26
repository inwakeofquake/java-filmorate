package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @NotNull
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private HashSet<Long> likes = new HashSet<>();

    public void addLike(Long userId) {
        this.likes.add(userId);
    }

    public void removeLike(Long userId) {
        if (this.likes.contains(userId) ) {
            this.likes.remove(userId);
        } else throw new NullPointerException("No such userId");
    }
}
