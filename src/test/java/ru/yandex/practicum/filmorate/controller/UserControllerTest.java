package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.lang.reflect.Executable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    /**
     * Граничные условия:
     * 1. Создать новго пользователя
     * 2. Обновить существующего пользователя
     * 3. Создать пользователя с существующим email
     * 4. Создать пользователя с пустым email
     * 5. Создать пользователя с email без @
     * 6. Создать пользователя с пустым логином
     * 7. Создать пользователя с проблелами в email
     * 8. Создать пользователя без имени, присвоим полю значение логина
     * 9. Создать пользователя с датой рождения в будущем
     **/

    User user;
    UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
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
                .id(2)
                .email("test22@test.com")
                .login("login2")
                .name("User2Name")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();
        userController.createUser(user);
        userController.updateUser(user2);
        List<User> userList = new ArrayList<>();
        userList.addAll(userController.findAllUsers());
        assertEquals(2, userList.get(0).getId());
    }

    @Test
    void shouldThrowExceptionThenAddExistingEmail() {
        User user2 = User.builder()
                .id(2)
                .email("test@test.com")
                .login("login2")
                .name("User2Name")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();
        userController.createUser(user);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user2));
        assertEquals(exception.getMessage(), exception.getMessage());
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
    void shouldThrowExceptionThenAddEmailWithSpaces() {
        User user2 = User.builder()
                .id(2)
                .email(" test@test.com")
                .login("login")
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