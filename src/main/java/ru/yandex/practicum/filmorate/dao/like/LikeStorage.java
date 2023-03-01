package ru.yandex.practicum.filmorate.dao.like;

import java.util.Set;

public interface LikeStorage {

    Long addLike(Long filmId, Long userId);

    Set<Long> findAllLikes(Long filmId);

    Long deleteLike(Long filmId, Long userId);

    Long deleteAllFilmLikes(Long filmId);

    Long deleteAllUserLikes(Long userId);
}
