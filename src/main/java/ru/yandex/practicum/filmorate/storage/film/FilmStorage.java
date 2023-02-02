package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    List<Film> findAllFilms();
    Film updateFilm(Film film);
    Film getFilmById(Integer filmId);
}
