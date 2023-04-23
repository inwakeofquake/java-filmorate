package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final FriendsDbStorage friendsDbStorage;

    @Test
    void testFilmorateDb() {

        User user = new User(1, "testLogin1", "testName1", "testEmail1@a.a",
                LocalDate.now().minusYears(35));
        userStorage.save(user);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u -> {
                            assertThat(u).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(u).hasFieldOrPropertyWithValue("email", "testEmail1@a.a");
                        }
                );

        user = new User(2, "testLogin2", "testName2", "testEmail2@a.a",
                LocalDate.now().minusYears(35));
        User savedUser = userStorage.save(user);
        assertThat(savedUser).isNotNull();

        user = new User(3, "testLogin3", "testName3", "testEmail3@a.a",
                LocalDate.now().minusYears(30));
        savedUser = userStorage.save(user);

        savedUser.setLogin("updatedLogin");
        savedUser.setName("updatedName");
        savedUser.setEmail("updatedEmail@a.a");
        savedUser.setBirthday(LocalDate.now().minusYears(25));

        User updatedUser = userStorage.update(savedUser);
        assertThat(updatedUser).isEqualTo(savedUser);

        User user1 = new User(4, "testLogin4", "testName4", "testEmail4@a.a",
                LocalDate.now().minusYears(20));
        User user2 = new User(5, "testLogin5", "testName5", "testEmail5@a.a",
                LocalDate.now().minusYears(15));
        User savedUser1 = userStorage.save(user1);
        User savedUser2 = userStorage.save(user2);

        ArrayList<User> allUsers = userStorage.getAll();
        assertThat(allUsers).contains(savedUser1, savedUser2);

        user = new User(6, "testLogin6", "testName6", "testEmail6@a.a",
                LocalDate.now().minusYears(10));
        savedUser = userStorage.save(user);
        boolean hasId = userStorage.hasId(savedUser.getId());
        assertThat(hasId).isTrue();

        user1 = new User(7, "testLogin7", "testName7", "testEmail7@a.a",
                LocalDate.now().minusYears(90));
        user2 = new User(8, "testLogin8", "testName8", "testEmail8@a.a",
                LocalDate.now().minusYears(54));
        savedUser1 = userStorage.save(user1);
        savedUser2 = userStorage.save(user2);

        friendsDbStorage.addFriend(savedUser1, savedUser2);

        List<User> friends = friendsDbStorage.getFriendsById(savedUser1.getId());
        List<Long> friendIds = friendsDbStorage.getFriendsIdsById(savedUser1.getId());

        assertThat(friends).contains(savedUser2);
        assertThat(friendIds).contains(savedUser2.getId());

        friendsDbStorage.removeFriend(savedUser1, savedUser2);

        friends = friendsDbStorage.getFriendsById(savedUser1.getId());
        friendIds = friendsDbStorage.getFriendsIdsById(savedUser1.getId());

        assertThat(friends).doesNotContain(savedUser2);
        assertThat(friendIds).doesNotContain(savedUser2.getId());

        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Genre genre = new Genre(1, "Комедия");
        genres.add(genre);
        genre = new Genre(6, "Боевик");
        genres.add(genre);
        Film film = new Film(1L, "testName1", "testDescription1",
                LocalDate.now(), 120, genres, new Mpa(1, "G"));

        Film savedFilm = filmStorage.save(film);
        assertThat(film).isEqualTo(savedFilm);

        Film loadedFilm = filmStorage.getById(1L);
        assertThat(film).isEqualTo(loadedFilm);

        film.setDescription("tdupdated");
        genres.remove(0);
        film.setGenres(genres);
        Film updatedFilm = filmStorage.update(film);
        assertThat(film).isEqualTo(updatedFilm);
    }
}