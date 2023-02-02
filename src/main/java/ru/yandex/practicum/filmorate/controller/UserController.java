package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("GET-запрос: получена коллекция всех пользователей, их кол-во: {}", userService.findAllUsers().size());
        return userService.findAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("POST-запрос: создан новый пользователь с id={}", user.getId());
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("PUT-запрос: обновлены данные пользователя: {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("GET-запрос: получен пользователь с id={}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("PUT-запрос: пользователь с id={} добавлен в друзья пользователю с id={}", friendId, id);
        userService.addFriend(id, friendId);
        return String.format("Пользователь с id=%d добавлен в друзья пользователю с id=%d", friendId, id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("DELETE-запрос: пользователь с id={} удален из друзей пользователя с id={}", friendId, id);
        userService.deleteFriend(id, friendId);
        return String.format("Пользователь с id=%d удален из друзей пользователя с id=%d", friendId, id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        log.info("GET-запрос: получен список друзей пользователя с id={}", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        List<User> commonFriendsList = userService.getMutualFriends(id, otherId);
        log.info("GET-запрос: получен список взаимных друзей пользователя с id={} и id={}." +
                "\n Список взаимных друзей: {}", id, otherId, commonFriendsList);
        return commonFriendsList;
    }
}
