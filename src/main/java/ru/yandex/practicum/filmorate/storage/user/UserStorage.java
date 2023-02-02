package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User createUser(User user);
    User updateUser(User user);
    Collection<User> findAllUsers();
    User getUserById(Integer userId);
}
