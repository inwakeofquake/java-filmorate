package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    private String login;
    private String name;
    private String email;
    private LocalDate birthday;


    public String getName(){
        if(name == null || name.isEmpty() || name.isBlank()) return login;
        return name;
    }
}
