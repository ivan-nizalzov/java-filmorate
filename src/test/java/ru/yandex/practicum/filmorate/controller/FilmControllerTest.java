package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.util.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void prepareTest() {
        this.filmController = new FilmController(
                new FilmService( new InMemoryFilmStorage(new HashMap<>())));
        film = Film.builder()
                .id(0)
                .name("FilmName")
                .description("FilmDescription")
                .releaseDate(LocalDate.of(2020, 10, 12))
                .duration(120)
                .build();
    }

    //>>>>>>>>>> Positive tests
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
                .id(0)
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
    void shouldGetFilmById() {
        filmController.addFilm(film);
        Film testFilm = filmController.getFilmById(film.getId());
        assertEquals("FilmName", testFilm.getName());
    }

    @Test
    void shouldAddLike() {
        filmController.addFilm(film);
        filmController.addLike(film.getId(), 1);

        assertEquals(1, filmController.getFilmById(film.getId()).getLikesIds().size());
        assertEquals(true, filmController.getFilmById(film.getId()).getLikesIds().contains(1));
    }

    @Test
    void shouldDeleteLike() {
        filmController.addFilm(film);
        filmController.addLike(film.getId(), 1);

        filmController.deleteLike(film.getId(), 1);
        assertEquals(true, filmController.getFilmById(film.getId()).getLikesIds().isEmpty());
    }

    @Test
    void shouldGetMostPopularFilms() {
        filmController.addFilm(film);
        filmController.addLike(film.getId(), 1);

        assertEquals(1, filmController.getMostPopularFilms(1).size());
        assertEquals("FilmName", filmController.getMostPopularFilms(1).get(0).getName());
    }
    // The end of positive tests
    //>>>>>>>>>> Negative tests
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

    @Test
    void shouldThrowExceptionThenDeletingInvalidLike() {
        filmController.addFilm(film);
        filmController.addLike(film.getId(), 1);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmController
                .deleteLike(film.getId(), 2));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void shouldThrowExceptionThenNumberOfPopularFilmsIsNegativeOrNull() {
        filmController.addFilm(film);
        filmController.addLike(film.getId(), 1);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController
                .getMostPopularFilms(0));
        assertEquals(exception.getMessage(), exception.getMessage());
        ValidationException exception2 = assertThrows(ValidationException.class, () -> filmController
                .getMostPopularFilms(-1));
        assertEquals(exception2.getMessage(), exception2.getMessage());
    }
    // The end of negative tests
}