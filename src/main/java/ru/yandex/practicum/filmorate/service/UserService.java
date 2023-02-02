package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId);
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (userId.equals(friendId)) {
            throw new ValidationException("Id пользователей не могут быть одинаковыми при добавлении в друзья.");
        }

        user.addFriend(friendId);
        friend.addFriend(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (!user.getFriendsId().contains(friend.getId())) {
            throw new ValidationException("Пользователь с id=" + friendId + " не был добавлен в друзья пользователю" +
                    " с id=" + userId);
        }

        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public List<User> getUserFriends(Integer userId) {
        User user = userStorage.getUserById(userId);

        return user.getFriendsId()
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Integer userId, Integer otherUserId) {

        if (userId.equals(otherUserId)) {
            throw new ValidationException("Id пользователей не могут быть одинаковыми для вывода списка общих друзей.");
        }
        if (userId == null || userId <= 0) {
            throw new NotFoundException("id пользователя не может быть меньше или равняться нулю, получен id=" + userId);
        }
        if (otherUserId == null || otherUserId <= 0) {
            throw new NotFoundException("id пользователя не может быть меньше или равняться нулю, получен id=" + otherUserId);
        }

        return getUserById(userId).getFriendsId()
                .stream()
                .filter(getUserById(otherUserId).getFriendsId()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

}
