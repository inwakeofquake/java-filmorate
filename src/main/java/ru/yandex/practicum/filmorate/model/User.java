package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id = 0;
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Login must contain only letters, digits and underscores.")
    private String login;
    private String name;
    @NotBlank
    @Email
    private String email;
    private LocalDate birthday;
    private Set<Long> friendIds = new HashSet<>();

    public String getName() {
        if (name == null || name.isBlank()) {
            return login;
        }
        return name;
    }

    public void addFriendId(Long friendId) {
        friendIds.add(friendId);
    }

    public void removeFriendId(Long friendId) {
        friendIds.remove(friendId);
    }
}
