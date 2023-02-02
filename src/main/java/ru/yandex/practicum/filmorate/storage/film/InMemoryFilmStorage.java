package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films;
    private static final AtomicInteger id = new AtomicInteger(0);

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        film.setId(id.incrementAndGet());
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public List<Film> findAllFilms() {
       // return new ArrayList<>(films.values());
        ArrayList<Film> tempList = new ArrayList<>();
        tempList.addAll(films.values());
        return tempList;
    }

    @Override
    public Film updateFilm(Film film) throws RuntimeException {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден.");
        }
        films.remove(film.getId());
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Фильм с id=" + filmId + " не найден.");
        }
        return films.get(filmId);
    }

    private Film validateFilm(Film film) {
        if (film.getName().isBlank() || film.getName().isEmpty()) {
            log.info("Название фильма пустое или поле film.name пустое.");
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
        return film;
    }
}
