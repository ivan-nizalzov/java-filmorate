package ru.yandex.practicum.filmorate.exception;

import org.springframework.stereotype.Component;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
