package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() throws NotFoundException {
        log.info("Запрос на получение всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") Long id) throws NotFoundException {
        log.info("Запрос на получение фильма по id");
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на создания фильма");
        return filmService.create(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) throws NotFoundException {
        log.info("Запрос на обновление фильма");
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Long addLikeFilm(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId
    ) throws NotFoundException {
        log.info("Запрос на добавление лайка фильму");
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Long removeLikeFilm(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId
    ) throws NotFoundException {
        log.info("Запрос на удаление лайка у фильма");
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> findPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count
    ) {
        log.info("Запрос на получение популярного фильма");
        return filmService.findPopularFilms(count);
    }
}
