package ru.yandex.practicum.filmorate.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private FriendshipStorage friendDao;

    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendshipStorage friendDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendDao = friendDao;
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();

        return User.builder().
                id(id).
                email(email).
                login(login).
                name(name).
                birthday(birthday).
                build();
    }

    @Override
    public User findById(long userId) throws NotFoundException {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId);
        if (users.size() < 1) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        return users.get(0);
    }

    @Override
    public List<User> findAll() throws NotFoundException {
        final String sqlQuery = "SELECT * FROM USERS";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
        if (users.size() == 0) {
            throw new NotFoundException("Users were not found.");
        }
        return users;
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection connection) -> {
            final String sqlQuery = "INSERT INTO USERS (NAME, EMAIL, LOGIN, BIRTHDAY)" +
                    " VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) throws NotFoundException {
        findById(user.getId());

        final String sqlQuery = "UPDATE USERS SET NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());

        log.info("Пользователь обновлен, USER_ID - {}", user.getId());
        return user;
    }

    @Override
    public Long delete(Long userId) {
        String deleteUser = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(deleteUser, userId);
        log.info("Запись с пользователем успешно удалена, его id - {}", userId);
        return userId;
    }

    @Override
    public Long addFriend(Long userId, Long friendId) {
        return friendDao.addFriend(userId, friendId);
    }

    @Override
    public Collection findUserFriendsById(Long userId) {
        return friendDao.findUserFriendsById(userId);
    }

    @Override
    public Collection findUserFriendsIdById(Long userId) {
        return friendDao.findUserFriendsIdById(userId);
    }

    @Override
    public Long deleteFriend(Long userId, Long friendId) {
        return friendDao.deleteFriend(userId, friendId);
    }

    @Override
    public Collection<User> findCommonFriends(Long firstUserId, Long secondUserId) {
        return friendDao.findCommonFriends(firstUserId, secondUserId);
    }
}
