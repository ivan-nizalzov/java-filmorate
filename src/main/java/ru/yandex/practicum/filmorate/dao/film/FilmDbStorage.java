package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dao.like.LikeStorage;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate  jdbcTemplate;
    private static GenreStorage genreDao;
    private static MpaStorage mpaDao;
    private static LikeStorage likeDao;
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreDao, MpaStorage mpaDao, LikeStorage likeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
    }
    public static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("FILM_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        long duration = rs.getLong("DURATION");
        MPA mpa = mpaDao.findById(rs.getLong("MPA_ID"));
        Set<Genre> genre = (Set<Genre>) genreDao.getGenreFromFilmId(id);
        Set<Long> likes = likeDao.findAllLikes(id);

        return Film.builder().
                id(id).
                name(name).
                description(description).
                releaseDate(releaseDate).
                duration(duration).
                mpa(mpa).
                likes(likes).
                genres(genre).
                build();
    }

    @Override
    public Film findById(long filmId) throws NotFoundException {
        final String sqlQuery ="SELECT * FROM FILMS WHERE FILM_ID = ?";

        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, filmId);
        if (films.size() < 1) {
            throw new NotFoundException("Film with id=" + filmId + " was not found.");
        }
        return films.get(0);
    }

    @Override
    public Collection<Film> findAll() throws NotFoundException {
        final String sqlQuery = "SELECT * FROM FILMS";
        final Collection<Film> films = jdbcTemplate.query(sqlQuery,(rs, rowNum) -> FilmDbStorage.makeFilm(rs, rowNum));
        /*if (films.size() == 0) {
            throw new NotFoundException("Films were not found.",filmId);
        }*/
        return films;
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)" +
                " VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update((Connection connection) -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        long newId = keyHolder.getKey().longValue();
        film.setId(newId);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreDao.addGenreForFilm(newId, genre.getId());
            }
        }

        return film;
    }

    @Override
    public Film update(Film film) throws NotFoundException {
        findById(film.getId());

        final String sqlQuery = "UPDATE FILMS SET NAME=?, DESCRIPTION=?, RELEASE_DATE=?," +
                " DURATION=?, MPA_ID=? WHERE FILM_ID=? ";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        genreDao.deleteByFilmId(film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreDao.addGenreForFilm(film.getId(), genre.getId());
            }
        }

        log.info("Фильм с id={} успешно обновлен", film.getId());
        return findById(film.getId());
    }

    @Override
    public Long deleteFilm(Long filmId) {
        final String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);

        log.info("Записи фильма с id={} успешно удалены", filmId);
        return filmId;
    }

    @Override
    public Collection<Film> findPopularFilms(Integer maxSize) {
        final String sqlQuery =
                "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE,f.DURATION, f.MPA_ID, COUNT(likes.USER_ID) " +
                        " FROM FILMS AS f " +
                        " LEFT JOIN FILM_LIKES AS likes ON f.FILM_ID = likes.FILM_ID " +
                        " GROUP BY f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID " +
                        " ORDER BY COUNT(likes.USER_ID) DESC " +
                        " LIMIT ?";

        log.info("Получен список популярных фильмов в кол-ве: {} ", maxSize);
        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> FilmDbStorage.makeFilm(rs,rowNum), maxSize));
    }
    @Override
    public Long addLike(Long filmId, Long userId) {
        return likeDao.addLike(filmId, userId);
    }
    @Override
    public Long deleteLike(Long filmId, Long userId) {
        return likeDao.deleteLike(filmId, userId);
    }
}