package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull
    private int id;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Login cannot be blank")
    @Pattern(regexp = "^[^\\s]+$", message = "Login cannot contain spaces")
    private String login;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;
}
