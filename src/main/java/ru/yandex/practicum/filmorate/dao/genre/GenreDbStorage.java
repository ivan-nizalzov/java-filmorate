package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("GENRE_ID");
        String name = rs.getString("NAME");

        return Genre.builder().
                id(id).
                name(name).
                build();
    }

    @Override
    public List<Genre> findAll() {
        final String sqlQuery = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
    }

    @Override
    public Genre findById(Long genreId) throws NotFoundException {
        final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs, rowNum), genreId);
        if (genres.size() < 1) {
            throw new NotFoundException("genre", genreId);
        }

        return genres.get(0);
    }

    @Override
    public Collection<Genre> getGenreFromFilmId(Long filmId) {
        final String sqlQuery = "SELECT G.GENRE_ID, G.NAME\n" +
                "FROM FILM_GENRE AS GF\n" +
                "LEFT JOIN GENRES G on G.GENRE_ID = GF.GENRE_ID\n" +
                "WHERE FILM_ID = ?\n" +
                "ORDER BY  G.GENRE_ID";

        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs, rowNum), filmId));
    }

    @Override
    public void addGenreForFilm(Long filmId, long genreId) {
        final String sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public void deleteByFilmId(Long filmId) {
        final String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

}