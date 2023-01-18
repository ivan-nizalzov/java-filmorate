package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.lang.reflect.Executable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    private User user;
    private UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(new UserService(new HashMap<>()));
        user = User.builder()
                .id(1)
                .email("test@test.com")
                .login("login")
                .name("UserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();
    }

    @Test
    void shouldCreateUser() {
        userController.createUser(user);
        assertEquals(1, userController.findAllUsers().size());
    }

    @Test
    void shouldUpdateUser() {
        User user2 = User.builder()
                .id(1)
                .email("test22@test.com")
                .login("login2")
                .name("UserNewName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();
        userController.createUser(user);
        userController.updateUser(user2);
        List<User> userList = new ArrayList<>();
        userList.addAll(userController.findAllUsers());
        assertEquals("login2", userList.get(0).getLogin());
    }

    @Test
    void shouldThrowExceptionThenAddEmptyEmail() {
        User user2 = User.builder()
                .id(2)
                .email("")
                .login("login2")
                .name("User2Name")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user2));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void shouldThrowExceptionThenAddEmailWithoutAtSign() {
        User user2 = User.builder()
                .id(2)
                .email("test.test.com")
                .login("login2")
                .name("User2Name")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user2));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void shouldThrowExceptionThenAddEmptyLogin() {
        User user2 = User.builder()
                .id(2)
                .email("test@test.com")
                .login("")
                .name("User2Name")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user2));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void shouldThrowExceptionThenAddLoginWithSpaces() {
        User user2 = User.builder()
                .id(2)
                .email("test@test.com")
                .login(" login")
                .name("User2Name")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user2));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void shouldThrowExceptionThenAddBirthdayInFuture() {
        User user2 = User.builder()
                .id(2)
                .email("test@test.com")
                .login("login")
                .name("User2Name")
                .birthday(LocalDate.of(2025, 10, 01))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user2));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void shouldBingLoginToNameFieldThenIsEmpty() {
        User user2 = User.builder()
                .id(2)
                .email("test@test.com")
                .login("login")
                .name("")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();
        userController.createUser(user2);
        List<User> userList = new ArrayList<>();
        userList.addAll(userController.findAllUsers());
        assertEquals("login", userList.get(0).getName());
    }
}