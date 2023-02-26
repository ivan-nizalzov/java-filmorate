package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class ModelCheck {

    public static void validateId(Integer id) {
        if (id == null) {
            log.info("Id некорректен, получен id={}", id);
            throw new ValidationException("Id не может быть пустым.");
        }
    }

    public static User validateUser(User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            log.info("Email пустой или поле user.email пустое.");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        } else if (user.getEmail().indexOf("@") == -1) {
            log.info("Email не содержит @.");
            throw new ValidationException("Адрес электронной почты должен содержать символ @.");
        } else if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.info("Логин пустой или поле user.login пустое.");
            throw new ValidationException("Логин не может быть пустым.");
        } else if (user.getLogin().indexOf(" ") != -1) {
            log.info("Логин содержит пробел/пробелы.");
            throw new ValidationException("Логин не может содержать пробелы.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения пользователя указана в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        } else if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Имя пользователя не указано, полю user.name присвоен логин.");
            user.setName(user.getLogin());
        }
        return user;
    }


    public static Film validateFilm(Film film) {
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
