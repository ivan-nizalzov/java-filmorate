package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private static final AtomicInteger id = new AtomicInteger(0);

    public Collection<User> findAllUsers() {
        log.debug("Успешно возвращена коллекция пользователей.");
        return users.values();
    }

    /**
     * Судя по ТЗ8, на данном этапе не проводится проверка email нового пользователя на пересечение с уже существующим.
     */
    public User createUser(User user) {
        validateUser(user);
        user.setId(id.incrementAndGet());
        users.put(user.getId(), user);
        log.debug("Успешно создан новый пользователь с id=" + user.getId());
        return user;
    }

    public User updateUser(User user) {
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id=" + user.getId() + " не найден.");
        }
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.debug("Пользователь с id=" + user.getId() + " успешно обновлен.");
        return user;
    }

    private User validateUser(User user) {
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
            log.info("Имя пользователя не указано, полю user.name присвон логин.");
            user.setName(user.getLogin());
        }
        return user;
    }
}
