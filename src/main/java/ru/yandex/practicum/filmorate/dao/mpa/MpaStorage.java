package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import java.util.Collection;

public interface MpaStorage {

    Collection<MPA> findAll();

    MPA findById(Long mpaId) throws NotFoundException;

    String findNameById(Long mpaId) throws NotFoundException;
}
