package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User findById(long userID) throws NotFoundException;

    List<User> findAll() throws NotFoundException;

    User create(User user);

    User update(User user) throws NotFoundException;

    Long delete(Long userId);

    Long addFriend(Long userId, Long friendId);

    Collection findUserFriendsById(Long userId);

    Collection findUserFriendsIdById(Long userId);

    Long deleteFriend(Long userId, Long friendId);

    Collection<User> findCommonFriends(Long firstUserId, Long secondUserId);
}
