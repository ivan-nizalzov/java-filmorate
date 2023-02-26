package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private Film film;

    @BeforeEach
    public void beforeEach() {
        film = Film.builder().
                id(1).
                name("ФильмТест").
                description("Здесь могла быть твоя реклама").
                releaseDate(LocalDate.of(2014, Month.MARCH, 17)).
                duration(180L).
                mpa(new MPA(1)).
                likes(new HashSet<>()).
                genres(Collections.singleton(new Genre(2l,"name"))).
                build();
        filmDbStorage.create(film);
    }
    @Test
    public void addFilmTest() throws NotFoundException {
        assertThat(filmDbStorage.findAll()).hasSize(1);
    }
    @Test
    public void findFilmByIdTest() throws NotFoundException {
        Film foundFilm = filmDbStorage.findByID(film.getId());
        assertThat(film.getId()).isEqualTo(foundFilm.getId());
        assertThat(film.getName()).isEqualTo(foundFilm.getName());
        assertThat(film.getDescription()).isEqualTo(foundFilm.getDescription());
        assertThat(film.getReleaseDate()).isEqualTo("2014-03-17");
        assertThat(film.getDuration()).isEqualTo(180L);
        assertThat(film.getMpa().getId()).isEqualTo(1);
    }
    @Test
    public void findAllNullTest() throws NotFoundException {
        filmDbStorage.deleteFilm(film.getId());
        List<Film> films = (List<Film>) filmDbStorage.findAll();
        assertThat(films).hasSize(0);
    }
    @Test
    public void findAllTest() throws NotFoundException {
        List<Film> films = (List<Film>) filmDbStorage.findAll();
        assertThat(films).hasSize(1);
    }
    @Test
    public void updateFilmTest() {
        Film filmReturn = filmDbStorage.create(film);
        Film filmUpdate = null;
        try {
            filmUpdate = filmDbStorage.update(Film.builder().
                    id(filmReturn.getId()).
                    name("ФильмТестОбновил").
                    description("Реклама").
                    releaseDate(LocalDate.of(2000, Month.DECEMBER, 17)).
                    duration(100L).
                    mpa(new MPA(3)).
                    likes(new HashSet<>()).
                    genres(Collections.singleton(new Genre(3l,"name"))).build());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        assertThat(filmUpdate.getName()).isEqualTo("ФильмТестОбновил");
        assertThat(filmUpdate.getDescription()).isEqualTo("Реклама");
        assertThat(filmUpdate.getReleaseDate()).isEqualTo("2000-12-17");
        assertThat(filmUpdate.getDuration()).isEqualTo(100L);
        assertThat(filmUpdate.getMpa().getId()).isEqualTo(3);
    }
    @Test
    public void deleteFilm() throws NotFoundException {
        assertThat(filmDbStorage.findAll()).hasSize(1);
        filmDbStorage.deleteFilm(film.getId());
        assertThat(filmDbStorage.findAll()).hasSize(0);
    }
}