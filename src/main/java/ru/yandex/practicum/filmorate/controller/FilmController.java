package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAllFilms() {
        log.info("GET-запрос: получить коллекция фильмов в кол-ве {}", filmService.findAllFilms().size());
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("POST-запрос: создать экземпляр класса Film: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("PUT-запрос: обновить фильм {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("GET-запрос: получить фильм с id={}", id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public String addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
        log.info("PUT-запрос: поставить лайк фильму с id={} пользователем с id={}", id, userId);
        log.info("Теперь у фильм кол-во лайков: {}", filmService.getFilmById(id).getLikesIds().size());
        return String.format("Фильму с id=%d поставлен лайк пользователем с id=%d", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
        log.info("DELETE-запрос: лайк фильму с id={} удален пользователем с id={}", id, userId);
        return String.format("Удален лайк фильму с id=%d пользователем с id=%d", id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false)
                                              @PathVariable Integer count) {
        if (count <= 0) {
            throw new ValidationException("Число фильмов не может быть меньше или равняться нулю.");
        }
        List<Film> popularFilmsList = filmService.getMostPopularFilms(count);
        log.info("GET-запрос: получить {} популярных фильмов." + "\n Список фильмов: {}", count, popularFilmsList);
        return popularFilmsList;
    }
}
