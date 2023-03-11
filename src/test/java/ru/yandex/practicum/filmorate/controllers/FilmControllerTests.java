package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllFilms() {
        ResponseEntity<List<Film>> response = restTemplate.exchange("/films", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Film>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testAddFilmWithValidData() {
        Film film = new Film(1, "Test Film 1", "Test Film 1 Description",
                LocalDate.of(1985,1,1), Duration.ofMinutes(200));
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(film.getName(), response.getBody().getName());
    }

    @Test
    void testUpdateFilmWithValidData() {
        Film film = new Film(1, "Test Film 2", "Test Film 2 Description",
                LocalDate.of(1980,1,1), Duration.ofMinutes(200));
        restTemplate.postForEntity("/films", film, Film.class);

        film.setName("Updated Test Film");
        ResponseEntity<Film> response = restTemplate.exchange("/films/" + film.getId(),
                HttpMethod.PUT, new HttpEntity<>(film), Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(film.getName(), response.getBody().getName());
    }

    @Test
    void testAddFilmWithNegativeDuration() {
        Film film = new Film(1, "Test Film 3", "Test Film 3 Description",
                LocalDate.of(1980,1,1), Duration.ofMinutes(-1));
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getDuration());
    }

    @Test
    void testAddFilmWithReleaseDateTooEarly() {
        Film film = new Film(1, "Test Film 5", "Test Film 5 Description",
                LocalDate.of(1895,12,27), Duration.ofMinutes(200));
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getReleaseDate());
    }

    @Test
    void testAddFilmWithLargeDescription() {
        Film film = new Film(1, "Test Film 6", "Test Film 6 Description Test Film 6 Description " +
                "Test Film 6 Description Test Film 6 Description Test Film 6 Description Test Film 6 Description" +
                " Test Film 6 Description Test Film 6 Description" +
                "Test Film 6 Description Test Film 6 Description \" +\n" +
                "                \"Test Film 6 Description Test Film 6 Description Test Film 6 Description Test Film 6 Description\" +\n" +
                "                \" Test Film 6 Description Test Film 6 Description",
                LocalDate.of(1895,12,29), Duration.ofMinutes(200));
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getDescription());
    }

    @Test
    void testUpdateFilmWithNegativeDuration() {
        Film film = new Film(1, "Test Film 6", "Test Film 6 Description",
                LocalDate.of(2000,1,1), Duration.ofMinutes(200));
        restTemplate.postForEntity("/films", film, Film.class);

        film.setDuration(Duration.ofMinutes(-10));
        ResponseEntity<Film> response = restTemplate.exchange("/films/" + film.getId(),
                HttpMethod.PUT, new HttpEntity<>(film), Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getDuration());
    }
}