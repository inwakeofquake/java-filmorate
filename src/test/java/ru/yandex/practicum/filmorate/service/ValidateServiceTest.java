package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ValidateServiceTest {

    @Test
    void validateUserTest() {
        ValidateService validateService = new ValidateService();

        User userWithInvalidId = new User(-1,"testlogin","John Smith","john@test.com",
                LocalDate.now().minusYears(35));
        assertThrows(RuntimeException.class, () -> validateService.validateUser(userWithInvalidId),
                "validateUser() should throw a RuntimeException when the user's ID is invalid");

        User userWithEmptyLogin = new User(1,"","John Brown","brown@test.com",
                LocalDate.now().minusYears(41));
        assertThrows(RuntimeException.class, () -> validateService.validateUser(userWithEmptyLogin),
                "validateUser() should throw a RuntimeException when the user's login is empty");

        User userWithEmptyName = new User(1,"testlogin","","john@test.com",
                LocalDate.now().minusYears(15));
        assertEquals(userWithEmptyName.getName(), userWithEmptyName.getLogin());

        User userWithWhitespaceName = new User(1,"testlogin"," ","john@test.com",
                LocalDate.now().minusYears(15));
        assertEquals(userWithWhitespaceName.getName(), userWithWhitespaceName.getLogin());

        User userWithNullName = new User(1,"testlogin",null,"john@test.com",
                LocalDate.now().minusYears(15));
        assertEquals(userWithNullName.getName(), userWithNullName.getLogin());

        User userWithFutureBirthday = new User(1,"testlogin","John Future","john@test.com",
                LocalDate.now().plusDays(1));
        assertThrows(RuntimeException.class, () -> validateService.validateUser(userWithFutureBirthday),
                "validateUser() should throw a RuntimeException when the user's birthday is in the future");

        User userWithNullBirthday = new User(1,"testlogin","","john@test.com",null);
        assertThrows(RuntimeException.class, () -> validateService.validateUser(userWithNullBirthday),
                "validateUser() should throw a RuntimeException when the user's birthday is null");

        User validUser = new User(1,"testlogin","John Smith","john@test.com",
                LocalDate.now().minusYears(35));
        assertDoesNotThrow(() -> validateService.validateUser(validUser),
                "validateUser() should not throw a RuntimeException when the user's name is valid");
    }

    @Test
    void validateFilmTest() {
        ValidateService validateService = new ValidateService();

        Film filmWithInvalidID = new Film(-1,"Test film name","Test film Description",
                LocalDate.now().minusYears(1), 90);
        assertThrows(RuntimeException.class, () -> validateService.validateFilm(filmWithInvalidID),
                "validateFilm() should throw a RuntimeException when the film's ID is invalid");

        Film filmWithNullName = new Film(1,null,"Test film Description",
                LocalDate.now().minusYears(1), 90);
        assertThrows(RuntimeException.class, () -> validateService.validateFilm(filmWithNullName),
                "validateFilm() should throw a RuntimeException when the film's name is null");

        Film filmWithEmptyName = new Film(1,"","Test film Description",
                LocalDate.now().minusYears(1), 90);
        assertThrows(RuntimeException.class, () -> validateService.validateFilm(filmWithEmptyName),
                "validateFilm() should throw a RuntimeException when the film's name is empty");

        Film filmWithWhitespaceName = new Film(1," ","Test film Description",
                LocalDate.now().minusYears(1), 90);
        assertThrows(RuntimeException.class, () -> validateService.validateFilm(filmWithWhitespaceName),
                "validateFilm() should throw a RuntimeException when the film's name has whitespace");

        Film filmWithLargeDescription = new Film(1,"Test film name","Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea " +
                "commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore" +
                " eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " +
                "officia deserunt mollit anim id est laborum.",
                LocalDate.now().minusYears(1), 90);
        assertThrows(RuntimeException.class, () -> validateService.validateFilm(filmWithLargeDescription),
                "validateFilm() should throw a RuntimeException when the film's description is more " +
                        "than 200 characters long");

        Film filmWithInvalidReleaseDate = new Film(1," ","Test film Description",
                LocalDate.of(1895,12,27), 90);
        assertThrows(RuntimeException.class, () -> validateService.validateFilm(filmWithInvalidReleaseDate),
                "validateFilm() should throw a RuntimeException when the film's release date is older " +
                        "than the first film ever released");

        Film filmWithNegativeDuration = new Film(1,"Test film name","Test film Description",
                LocalDate.now().minusYears(5), -120);
        assertThrows(RuntimeException.class, () -> validateService.validateFilm(filmWithNegativeDuration),
                "validateFilm() should throw a RuntimeException when the film's duration is negative");
    }
}