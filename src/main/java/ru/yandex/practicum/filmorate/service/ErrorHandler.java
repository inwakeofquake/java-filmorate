package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.NoSuchIdException;

import java.util.Map;

@Slf4j
@RestControllerAdvice("ru.yandex.practicum.filmorate")
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNoSuchId(final NoSuchIdException e) {
        log.error("No such ID error occurred. Response code: {}", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                Map.of(
                        "error", "No such ID",
                        "errorMessage", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> invalidInput(final InvalidInputException e) {
        log.error("Invalid input error occurred. Response code: {}", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                Map.of(
                        "error", "Invalid input",
                        "errorMessage", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    public ResponseEntity<Map<String, String>> exceptionFound(final RuntimeException e) {
        log.error("Exception found. Response code: {}", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(
                Map.of(
                        "error", "Exception found",
                        "errorMessage", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

