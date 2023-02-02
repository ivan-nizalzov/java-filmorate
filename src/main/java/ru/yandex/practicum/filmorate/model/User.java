package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    //@JsonIgnore
    private final Set<Integer> friendsId = new HashSet<>();

    public void addFriend(Integer id) {
        friendsId.add(id);
    }

    public void deleteFriend(Integer id) {
        friendsId.remove(id);
    }
}
