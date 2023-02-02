package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private static final AtomicInteger id = new AtomicInteger(0);

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        user.setId(id.incrementAndGet());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден.");
        }
        users.remove(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Integer userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден.");
        }
        return users.get(userId);
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
            log.info("Имя пользователя не указано, полю user.name присвоен логин.");
            user.setName(user.getLogin());
        }
        return user;
    }
}
