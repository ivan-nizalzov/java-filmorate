package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ModelCheck;

import java.util.Collection;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmStorage userStorage;

    public FilmService(FilmStorage filmDbStorage, FilmStorage userDbStorage) {
        this.filmStorage = filmDbStorage;
        this.userStorage = userDbStorage;
    }

    public Collection<Film> findAll() throws NotFoundException {
        return filmStorage.findAll();
    }

    public Film getFilmById(Long id) throws NotFoundException {
        return filmStorage.findById(id);
    }

    public Film create(Film film) {
        ModelCheck.validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) throws NotFoundException {
        return filmStorage.update(film);
    }

    public Collection<Film> findPopularFilms(Integer count) {
        return filmStorage.findPopularFilms(count);
    }

    public Long deleteLike(Long filmId, Long userId) throws NotFoundException {
        //filmStorage.findById(filmId);
        //userStorage.findById(userId);
        checkFilmInDb(filmId);
        checkUserInDb(userId);
        return filmStorage.deleteLike(filmId, userId);
    }

    public Long addLike(Long filmId, Long userId) throws NotFoundException {
        //filmStorage.findById(filmId);
        //userStorage.findById(userId);
        checkFilmInDb(filmId);
        checkUserInDb(userId);
        return filmStorage.addLike(filmId, userId);
    }

    private void checkFilmInDb(Long filmId) throws NotFoundException {
        filmStorage.findById(filmId);
    }

    private void checkUserInDb(Long userId) throws NotFoundException {
        userStorage.findById(userId);
    }
}
