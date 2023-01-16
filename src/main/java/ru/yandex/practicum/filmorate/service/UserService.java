package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public Collection<User> findAllUsers() {
        return users.values();
    }

    public User createUser(User user) {
        validateUser(user);
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь с данным email уже существует: " + user.getEmail());
        }
        users.put(user.getEmail(), user);
        return user;
    }

    public User updateUser(User user) {
        validateUser(user);
        if (users.containsKey(user.getEmail())) {
            users.remove(user.getEmail());
        }
        users.put(user.getEmail(), user);
        return user;
    }

    private void validateUser(User user) {
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
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Имя пользователя не указано, полю user.name присвон логин.");
            user.setName(user.getLogin());
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения пользователя указана в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
