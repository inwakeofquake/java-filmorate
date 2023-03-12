package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Login must contain only letters, digits and underscores.")
    private String login;
    private String name;
    @NotBlank
    @Email
    private String email;
    private LocalDate birthday;

    public String getName(){
        if(name == null || name.isEmpty() || name.isBlank()) {
            return login;
        }
        return name;
    }
}
