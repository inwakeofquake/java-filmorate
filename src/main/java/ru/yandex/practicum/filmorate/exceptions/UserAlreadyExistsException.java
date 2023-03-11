package ru.yandex.practicum.filmorate.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User with this email already exists");
    }
}
