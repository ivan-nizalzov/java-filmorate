package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ModelCheck;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() throws NotFoundException {
        return userStorage.findAll();
    }

    public User create(User user) {
        ModelCheck.validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) throws UserNotFoundException, NotFoundException {
        return userStorage.update(user);
    }

    public User findById(Long userId) throws NotFoundException {
        return userStorage.findById(userId);
    }

    public Long addFriend(Long userId, Long friendId) throws NotFoundException {
        //userStorage.findById(userId);
        //userStorage.findById(friendId);
        checkUserInDb(userId);
        checkUserInDb(friendId);
        return userStorage.addFriend(userId, friendId);
    }

    public List<User> findAllUserFriendsById(Long userId) throws NotFoundException {
        userStorage.findById(userId);
        return (List<User>) userStorage.findUserFriendsById(userId);
    }

    public void deleteFriend(Long userId, Long friendId) throws NotFoundException {
        //userStorage.findById(userId);
        //userStorage.findById(friendId);
        checkUserInDb(userId);
        checkUserInDb(friendId);
        Collection userFriends = userStorage.findUserFriendsIdById(userId);
        if (!userFriends.contains(friendId)) {
            throw new NotFoundException(userId + ", не друг пользователю " + friendId);
        }
        userStorage.deleteFriend(userId, friendId);
        log.info(userId + " удален из друзей " + friendId);
    }

    public List<User> findCommonFriends(Long firstUserId, Long secondUserId) throws NotFoundException {
        //userStorage.findById(firstUserId);
        //userStorage.findById(secondUserId);
        checkUserInDb(firstUserId);
        checkUserInDb(secondUserId);
        return (List<User>) userStorage.findCommonFriends(firstUserId, secondUserId);
    }

    private void checkUserInDb(Long userId) throws NotFoundException {
        userStorage.findById(userId);
    }
}
