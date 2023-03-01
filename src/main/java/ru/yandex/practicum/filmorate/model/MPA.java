package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MPA {
    private String name;
    private long id;

    public MPA(long id) {
        this.id = id;
    }
}