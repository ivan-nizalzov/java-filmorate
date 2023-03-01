package ru.yandex.practicum.filmorate.dao.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Component
@Slf4j
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    public Long addFriend(Long userId, Long friendId) {
        final String sqlQuery = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);

        log.info("Юзер с id={} успешно добавлен в друзья юзеру с id={}", friendId, userId);
        return userId;
    }
    @Override
    public Long deleteFriend(Long userId, Long friendId) {
        final String sqlQuery = "DELETE FROM FRIENDSHIP WHERE " +
                " USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);

        log.info("Юзер с id={} успешно удален из друзей юзера с id={}", friendId, userId);
        return userId;
    }
    @Override
    public Collection<Long> findUserFriendsIdById(Long userId) {
        final String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE " +
                " USER_ID = ?";
        return jdbcTemplate.query(
                sqlQuery,
                (rs, rowNum) -> rs.getLong("FRIEND_ID"),
                userId);
    }
    @Override
    public Collection<User> findUserFriendsById(Long userId) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID IN " +
                "(SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs,rowNum), userId);
    }
    @Override
    public Collection<User> findCommonFriends(Long firstUserId, Long secondUserId) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID IN " +
                "(SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID IN " +
                "(Select FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?))";

        return jdbcTemplate.query(
                sqlQuery,
                (rs, rowNum) -> makeUser(rs,rowNum),
                firstUserId, secondUserId);
    }
}
