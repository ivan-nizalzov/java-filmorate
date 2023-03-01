package ru.yandex.practicum.filmorate.exception;

import org.springframework.stereotype.Component;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
