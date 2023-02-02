package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

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
    public void prepareTest() {
        this.userController = new UserController(new UserService(new InMemoryUserStorage(new HashMap<>())));
        user = User.builder()
                .id(1)
                .email("test@test.com")
                .login("login")
                .name("UserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();
    }

    //>>>>>>>>>> Positive tests
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
    void shouldGetUserById() {
        userController.createUser(user);
        User testUser = userController.getUserById(1);
        assertEquals("UserName", testUser.getName());
    }

    @Test
    void shouldAddFriend() {
        User friend = User.builder()
                .id(2)
                .email("test23@test.com")
                .login("login2")
                .name("NewUserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        userController.createUser(user);
        userController.createUser(friend);
        userController.addFriend(user.getId(), friend.getId());

        assertEquals(1, userController.getUserFriends(user.getId()).size());
        assertEquals(1, userController.getUserFriends(friend.getId()).size());
    }

    @Test
    void shouldDeleteFriend() {
        User friend = User.builder()
                .id(2)
                .email("test23@test.com")
                .login("login2")
                .name("NewUserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        userController.createUser(user);
        userController.createUser(friend);
        userController.addFriend(user.getId(), friend.getId());
        userController.deleteFriend(user.getId(), friend.getId());

        assertEquals(0, userController.getUserFriends(user.getId()).size());
        assertEquals(0, userController.getUserFriends(friend.getId()).size());
    }

    @Test
    void shouldGetUserFriends() {
        User friend = User.builder()
                .id(2)
                .email("test23@test.com")
                .login("login2")
                .name("NewUserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        userController.createUser(user);
        userController.createUser(friend);
        userController.addFriend(user.getId(), friend.getId());

        assertEquals(1, userController.getUserFriends(user.getId()).size());
        assertEquals("NewUserName", userController.getUserFriends(user.getId()).get(0).getName());
    }

    @Test
    void shouldGetMutualFriends() {
        User user2 = User.builder()
                .id(2)
                .email("test23@test.com")
                .login("login2")
                .name("NewUserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        User user3 = User.builder()
                .id(2)
                .email("test24@test.com")
                .login("login3")
                .name("AnotherUserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        userController.createUser(user);
        userController.createUser(user2);
        userController.createUser(user3);

        userController.addFriend(user.getId(), user2.getId());
        userController.addFriend(user2.getId(), user3.getId());

        assertEquals(1, userController.getMutualFriends(user.getId(), user3.getId()).size());
        assertEquals("NewUserName", userController.getMutualFriends(user.getId(), user3.getId())
                .get(0).getName());
    }
    // The end of positive tests

    //>>>>>>>>>> Negative tests
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

    @Test
    void shouldThrowExceptionThenGetUserByInvalidId() {
        userController.createUser(user);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userController.getUserById(2));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void shouldThrowExceptionThenAddingSameId() {
        User friend = User.builder()
                .id(2)
                .email("test23@test.com")
                .login("login2")
                .name("NewUserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        userController.createUser(user);
        userController.createUser(friend);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController
                .addFriend(user.getId(), user.getId()));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void shouldThrowExceptionThenDeletingInvalidFriendOrSameId() {
        User friend = User.builder()
                .id(2)
                .email("test23@test.com")
                .login("login2")
                .name("NewUserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        userController.createUser(user);
        userController.createUser(friend);
        userController.addFriend(user.getId(), friend.getId());

        //Invalid id
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userController
                .deleteFriend(user.getId(), 3));
        assertEquals(exception.getMessage(), exception.getMessage());

        //Same id
        ValidationException exception2 = assertThrows(ValidationException.class, () -> userController
                .deleteFriend(user.getId(), user.getId()));
        assertEquals(exception2.getMessage(), exception2.getMessage());
    }


    @Test
    void shouldThrowExceptionThenGetMutualFriendsWithSameId() {
        User user2 = User.builder()
                .id(2)
                .email("test23@test.com")
                .login("login2")
                .name("NewUserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        User user3 = User.builder()
                .id(2)
                .email("test24@test.com")
                .login("login3")
                .name("AnotherUserName")
                .birthday(LocalDate.of(1990, 10, 01))
                .build();

        userController.createUser(user);
        userController.createUser(user2);
        userController.createUser(user3);

        userController.addFriend(user.getId(), user2.getId());
        userController.addFriend(user2.getId(), user3.getId());

        //Same id
        ValidationException exception2 = assertThrows(ValidationException.class, () -> userController
                .getMutualFriends(user.getId(), user.getId()));
        assertEquals(exception2.getMessage(), exception2.getMessage());
    }
    // The end of negative tests
}