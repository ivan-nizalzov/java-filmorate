package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {

    List<Genre> findAll();

    Genre findById(Long genreId) throws NotFoundException;

    Collection<Genre> getGenreFromFilmId(Long filmId);

    void addGenreForFilm(Long filmId, long genreId);

    void deleteByFilmId(Long filmId);
}
