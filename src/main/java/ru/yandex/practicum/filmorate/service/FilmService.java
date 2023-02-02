package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film updateFilm(Film film) throws RuntimeException {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(userId);
        filmStorage.updateFilm(film);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId);

        if (userId <= 0) {
            throw new NotFoundException("id не может быть меньше или равняться нулю, получен id=" + userId);
        }
        if (!film.getLikesIds().contains(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не ставил лайк фильму с id=" + filmId + ".");
        }

        film.deleteLike(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikesIds().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
