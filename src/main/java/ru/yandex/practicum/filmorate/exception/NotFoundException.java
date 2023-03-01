package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends SQLException {
    private final String name;
    private final Long id;

    public NotFoundException(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public NotFoundException(String name) {
        this.name = name;
        this.id = 0l;
    }
}
