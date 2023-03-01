package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = User.builder().
                id(1).
                email("test@email.com").
                login("TestUser").
                name("Test").
                birthday(LocalDate.of(2000, Month.MARCH, 17)).
                build();
        userDbStorage.create(user);
    }
    @Test
    public void addUserTest() throws NotFoundException {
        assertThat(userDbStorage.findAll()).hasSize(1);
    }
    @Test
    public void updateUserTest() throws NotFoundException {
        User returnUSer = userDbStorage.update(
                User.builder().
                id(1).
                email("testUpdate@email.com").
                login("TestUserUpdate").
                name("TestUpdate").
                birthday(LocalDate.of(1995, Month.MARCH, 17)).
                build());
        assertThat(returnUSer.getId()).isEqualTo(1L);
        assertThat(returnUSer.getEmail()).isEqualTo("testUpdate@email.com");
        assertThat(returnUSer.getLogin()).isEqualTo("TestUserUpdate");
        assertThat(returnUSer.getName()).isEqualTo("TestUpdate");
        assertThat(returnUSer.getBirthday()).isEqualTo("1995-03-17");
    }
    @Test
    public void deleteUserTest() throws NotFoundException {
        assertThat(userDbStorage.findAll()).hasSize(1);
        userDbStorage.delete(1L);
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userDbStorage.findAll());
        assertEquals(null, exception.getMessage());
    }
    @Test
    public void findAllTest() throws NotFoundException {
        List<User> users = userDbStorage.findAll();
        assertThat(users).hasSize(1);
    }
    @Test
    public void shouldReturnUser_Id_1() throws NotFoundException {
        User foundUser = userDbStorage.findById(1L);
        assertThat(foundUser.getId()).isEqualTo(1L);
        assertThat(foundUser.getEmail()).isEqualTo("test@email.com");
        assertThat(foundUser.getLogin()).isEqualTo("TestUser");
        assertThat(foundUser.getName()).isEqualTo("Test");
        assertThat(foundUser.getBirthday()).isEqualTo("2000-03-17");
    }
}