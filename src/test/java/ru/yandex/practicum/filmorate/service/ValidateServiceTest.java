package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidateServiceTest {

    ValidateService validateService = new ValidateService();

    @Test
    void validateUserTest() {
        System.out.println(validateService);
        User userWithInvalidId = new User(-1, "testlogin", "John Smith", "john@test.com",
                LocalDate.now().minusYears(35), null);
        assertThrows(InvalidInputException.class, () -> validateService.validateUser(userWithInvalidId),
                "validateUser() should throw a RuntimeException when the user's ID is invalid");

        User userWithEmptyLogin = new User(1, "", "John Brown", "brown@test.com",
                LocalDate.now().minusYears(41), null);
        assertThrows(InvalidInputException.class, () -> validateService.validateUser(userWithEmptyLogin),
                "validateUser() should throw a RuntimeException when the user's login is empty");

        User userWithEmptyName = new User(1, "testlogin", "", "john@test.com",
                LocalDate.now().minusYears(15), null);
        assertEquals(userWithEmptyName.getName(), userWithEmptyName.getLogin());

        User userWithWhitespaceName = new User(1, "testlogin", " ", "john@test.com",
                LocalDate.now().minusYears(15), null);
        assertEquals(userWithWhitespaceName.getName(), userWithWhitespaceName.getLogin());

        User userWithNullName = new User(1, "testlogin", null, "john@test.com",
                LocalDate.now().minusYears(15), null);
        assertEquals(userWithNullName.getName(), userWithNullName.getLogin());

        User userWithFutureBirthday = new User(1, "testlogin", "John Future", "john@test.com",
                LocalDate.now().plusDays(1), null);
        assertThrows(InvalidInputException.class, () -> validateService.validateUser(userWithFutureBirthday),
                "validateUser() should throw a RuntimeException when the user's birthday is in the future");

        User userWithNullBirthday = new User(1, "testlogin", "",
                "john@test.com", null, null);
        assertThrows(InvalidInputException.class, () -> validateService.validateUser(userWithNullBirthday),
                "validateUser() should throw a RuntimeException when the user's birthday is null");

        User validUser = new User(1, "testlogin", "John Smith", "john@test.com",
                LocalDate.now().minusYears(35), new HashSet<>());
        assertDoesNotThrow(() -> validateService.validateUser(validUser),
                "validateUser() should not throw a RuntimeException when the user's name is valid");
    }

    @Test
    void validateFilmTest() {
        Film filmWithInvalidID = new Film(-1, "Test film name", "Test film Description",
                LocalDate.now().minusYears(1), 90, new HashSet<Long>(1));
        assertThrows(InvalidInputException.class, () -> validateService.validateFilm(filmWithInvalidID),
                "validateFilm() should throw a RuntimeException when the film's ID is invalid");

        Film filmWithNullName = new Film(1, null, "Test film Description",
                LocalDate.now().minusYears(1), 90, new HashSet<Long>(1));
        assertThrows(InvalidInputException.class, () -> validateService.validateFilm(filmWithNullName),
                "validateFilm() should throw a RuntimeException when the film's name is null");

        Film filmWithEmptyName = new Film(1, "", "Test film Description",
                LocalDate.now().minusYears(1), 90, new HashSet<Long>(1));
        assertThrows(InvalidInputException.class, () -> validateService.validateFilm(filmWithEmptyName),
                "validateFilm() should throw a RuntimeException when the film's name is empty");

        Film filmWithWhitespaceName = new Film(1, " ", "Test film Description",
                LocalDate.now().minusYears(1), 90, new HashSet<Long>(1));
        assertThrows(InvalidInputException.class, () -> validateService.validateFilm(filmWithWhitespaceName),
                "validateFilm() should throw a RuntimeException when the film's name has whitespace");

        Film filmWithLargeDescription = new Film(1, "Test film name", "Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea " +
                "commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore" +
                " eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " +
                "officia deserunt mollit anim id est laborum.",
                LocalDate.now().minusYears(1), 90, new HashSet<Long>(1));
        assertThrows(InvalidInputException.class, () -> validateService.validateFilm(filmWithLargeDescription),
                "validateFilm() should throw a RuntimeException when the film's description is more " +
                        "than 200 characters long");

        Film filmWithInvalidReleaseDate = new Film(1, " ", "Test film Description",
                LocalDate.of(1895, 12, 27), 90, new HashSet<Long>(1));
        assertThrows(InvalidInputException.class, () -> validateService.validateFilm(filmWithInvalidReleaseDate),
                "validateFilm() should throw a RuntimeException when the film's release date is older " +
                        "than the first film ever released");

        Film filmWithNegativeDuration = new Film(1, "Test film name", "Test film Description",
                LocalDate.now().minusYears(5), -120, new HashSet<Long>(1));
        assertThrows(InvalidInputException.class, () -> validateService.validateFilm(filmWithNegativeDuration),
                "validateFilm() should throw a RuntimeException when the film's duration is negative");
    }
}