package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    //@JsonIgnore
    private final Set<Integer> likesIds = new HashSet<>();

    public void addLike(Integer id) {
        likesIds.add(id);
    }

    public void deleteLike(Integer id) {
        likesIds.remove(id);
    }
}
