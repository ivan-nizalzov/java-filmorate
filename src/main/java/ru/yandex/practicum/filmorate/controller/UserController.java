package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService = new UserService();

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        log.debug("POST-запрос: создать нового пользователя.");
        userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("PUT-запрос: обновить данные пользователя.");
        return userService.updateUser(user);
    }
}
