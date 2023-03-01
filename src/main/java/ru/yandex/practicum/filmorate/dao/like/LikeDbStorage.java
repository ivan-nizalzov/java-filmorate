package ru.yandex.practicum.filmorate.dao.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long addLike(Long filmId, Long userId) {
        final String sqlQuery = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return filmId;
    }

    @Override
    public Set<Long> findAllLikes(Long filmId) {
        String getAllLikes = "SELECT USER_ID FROM FILM_LIKES WHERE USER_ID = ?";
        return new HashSet<>(jdbcTemplate.query(
                getAllLikes,
                (rs, rowNum) -> rs.getLong("USER_ID"),
                filmId));
    }

    @Override
    public Long deleteLike(Long filmId, Long userId) {
        final String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return filmId;
    }

    @Override
    public Long deleteAllFilmLikes(Long filmId) {
        final String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        return filmId;
    }

    @Override
    public Long deleteAllUserLikes(Long userId) {
        final String sqlQuery = "DELETE FROM FILM_LIKES WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, userId);
        return userId;
    }
}