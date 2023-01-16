package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Constants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FilmService {
    private final Map<String, Film> films = new HashMap<>();

    public Film addFilm(Film film) {
        validateFilm(film);
        if (films.containsKey(film.getDescription())) {
            throw new ValidationException("Такой фильм уже существует: " + film.getDescription());
        }
        films.put(film.getDescription(), film);
        return film;
    }

    public Collection<Film> findAllFilms() {
        return films.values();
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        if (films.containsKey(film.getDescription())) {
            films.remove(film.getDescription());
        }
        films.put(film.getDescription(), film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getDescription().isBlank() || film.getDescription().isEmpty()) {
            log.info("Название фильма пустое или поле film.description пустое.");
            throw new ValidationException("Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > Constants.MAX_LENGTH_OF_DESCRIPTION) {
            log.info("Кол-во символов в описании фильма превысило максимально допустимое.");
            throw new ValidationException("Описание фильма не может превышать 200 символов.");
        } else if (film.getReleaseDate().isBefore(Constants.EARLIEST_DATE_OF_RELEASE)) {
            log.info("Дата релиза фильма ранее 28.12.1895.");
            throw new ValidationException("Дата релиза должна быть не раньше 28.12.1895 (первый фильм в истории).");
        } else if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма меньше нуля.");
            throw new ValidationException("Продолжительность фильма не может быть меньше нуля или равняться нулю.");
        }
    }
}
