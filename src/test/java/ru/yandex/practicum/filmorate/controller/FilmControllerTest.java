package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class FilmControllerTest {
    /**
     * Граничные условия:
     * 1. Добавить новый фильм
     * 2. Обновить фильм
     * 3. Получить список всех фильмов (2  фильма)
     * 4. Добавить фильм с пустым названием
     * 5. Добавить фильм с описанием, превышающим максимальное допустимое кол-во символов
     * 6. Добавить фильм с датой релиза ранее 28.12.1895
     * 7. Добавить фильм с продолжительностью меньше нуля
     **/

    FilmController filmController;
    Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = Film.builder()
                .id(1)
                .name("FilmName")
                .description("FilmDescription")
                .releaseDate(LocalDate.of(2020, 10, 12))
                .duration(120)
                .build();
    }

    @Test
    void shouldAddFilm() {
        filmController.addFilm(film);
        assertEquals(1, filmController.findAllFilms().size());
    }

    @Test
    void shouldUpdateFilm() {
        Film film2 = Film.builder()
                .id(1)
                .name("FilmName")
                .description("FilmNewDescription")
                .releaseDate(LocalDate.of(2020, 10, 12))
                .duration(130)
                .build();
        filmController.addFilm(film);
        filmController.updateFilm(film2);
        List<Film> filmList = new ArrayList<>();
        filmList.addAll(filmController.findAllFilms());
        assertEquals("FilmNewDescription", filmList.get(0).getDescription());
    }

    @Test
    void shouldGetAllFilms() {
        Film film2 = Film.builder()
                .id(2)
                .name("NewFilmName")
                .description("Film2Description")
                .releaseDate(LocalDate.of(2020, 10, 12))
                .duration(130)
                .build();
        filmController.addFilm(film2);
        filmController.addFilm(film);
        assertEquals(2, filmController.findAllFilms().size());
    }

    @Test
    void shouldThrowExceptionThenAddEmptyName() {
        Film film2 = Film.builder()
                .id(2)
                .name("")
                .description("Film2Description")
                .releaseDate(LocalDate.of(2020, 10, 12))
                .duration(130)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
        assertEquals(exception.getMessage(), exception.getMessage());
        assertEquals(0, filmController.findAllFilms().size());
    }

    @Test
    void shouldTrowExceptionThenAddTooLongDescription() {
        Film film2 = Film.builder()
                .id(2)
                .name("Film2Name")
                .description(Constants.TOO_LONG_DESCRIPTION)
                .releaseDate(LocalDate.of(2020, 10, 12))
                .duration(130)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
        assertEquals(exception.getMessage(), exception.getMessage());
        assertEquals(0, filmController.findAllFilms().size());
    }

    @Test
    void shouldThrowExceptionThenAddTooEarlierDateRelease() {
        Film film2 = Film.builder()
                .id(2)
                .name("Film2Name")
                .description("Film2Description")
                .releaseDate(LocalDate.of(1890, 10, 12))
                .duration(130)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
        assertEquals(exception.getMessage(), exception.getMessage());
        assertEquals(0, filmController.findAllFilms().size());
    }

    @Test
    void shouldThrowExceptionThenAddNegativeDuration() {
        Film film2 = Film.builder()
                .id(2)
                .name("Film2Name")
                .description("Film2Description")
                .releaseDate(LocalDate.of(1990, 10, 12))
                .duration(-1)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
        assertEquals(exception.getMessage(), exception.getMessage());
        assertEquals(0, filmController.findAllFilms().size());
    }
}