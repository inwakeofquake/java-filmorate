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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange("/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testAddUser() {
        User user = new User(1, "test@example.com", "testuser", "Test User",
                LocalDate.now().minusYears(20));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getEmail(), response.getBody().getEmail());
    }

    @Test
    void testUpdateUser() {
        User user = new User(1, "test1@example.com", "testuser", "Test User",
                LocalDate.now().minusYears(20));
        restTemplate.postForEntity("/users", user, User.class);

        user.setName("Updated Test User");
        ResponseEntity<User> response = restTemplate.exchange("/users", HttpMethod.PUT,
                new HttpEntity<>(user), User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getName(), response.getBody().getName());
    }

    @Test
    void testAddUserWithBlankEmail() {
        User user = new User(2, " ", "testuser", "Test User",
                LocalDate.now().minusYears(20));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getEmail());
    }

    @Test
    void testUpdateUserWithInvalidEmail() {
        User user = new User(1, "test2@example.com", "testuser", "Test User",
                LocalDate.now().minusYears(20));
        restTemplate.postForEntity("/users", user, User.class);

        user.setEmail("---");
        ResponseEntity<User> response = restTemplate.exchange("/users", HttpMethod.PUT,
                new HttpEntity<>(user), User.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getEmail());
    }

    @Test
    void testAddUserWithSpaceInLogin() {
        User user = new User(2, "test3@test.com", "test user", "Test User",
                LocalDate.now().minusYears(20));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getLogin());
    }

    @Test
    void testAddUserWithInvalidName() {
        User user = new User(2, "test4@test.com", "testuser", "",
                LocalDate.now().minusYears(20));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getName());
    }

    @Test
    void testAddUserWithFutureBirthday() {
        User user = new User(2, "test5@test.com", "test user", "Test User",
                LocalDate.of(2023, 3, 30));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getBirthday());
    }
}
