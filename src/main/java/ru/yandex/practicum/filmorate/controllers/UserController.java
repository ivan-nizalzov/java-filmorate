package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() throws NotFoundException {
        log.info("Запрос на получение всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") Long id) throws UserNotFoundException, NotFoundException {
        log.info("Запрос на получение пользователей по id");
        return userService.findById(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Запрос на создание пользователя");
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws UserNotFoundException, NotFoundException {
        log.info("Запрос на обновление пользователя");
        return userService.update(user);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(final UserNotFoundException e) {
        return Map.of(
                "error", "UserNotFoundException",
                "errorMessage", e.getMessage()
        );
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addInFriends(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId
    ) throws NotFoundException {
        log.info("Запрос на добавление в друзья с id {} и id {}",
                userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId
    ) throws UserNotFoundException, NotFoundException {
        log.info("Запрос на удаление из друзей id {} и id {}",
                id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findAllFriendsById(@PathVariable("id") Long UserID) throws NotFoundException {
        log.info("Запрос на получение друзей id {}", UserID);
        return userService.findAllUserFriendsById(UserID);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @PathVariable("id") Long id,
            @PathVariable("otherId") Long otherId
    ) throws NotFoundException {
        log.info("Запрос на получение общих друзей id {} и id {}", id, otherId);
        return userService.findCommonFriends(id, otherId);
    }
}
