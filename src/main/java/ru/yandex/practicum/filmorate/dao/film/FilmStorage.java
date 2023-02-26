package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.util.Collection;

public interface FilmStorage {

    Film findByID(long filmId) throws NotFoundException;

    Collection<Film> findAll() throws NotFoundException;

    Film create(@Valid @RequestBody Film film);

    Film update(Film film) throws NotFoundException;

    Collection<Film> findPopularFilms(Integer maxSize);

    Long addLike(Long filmId, Long userId);

    Long deleteLike(Long filmId, Long userId);

    Long deleteFilm(Long filmId);
}
