package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService = new FilmService();

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.debug("POST-запрос: добавить новый фильм.");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("PUT-запрос: обновить фильм.");
        return filmService.updateFilm(film);
    }
}
