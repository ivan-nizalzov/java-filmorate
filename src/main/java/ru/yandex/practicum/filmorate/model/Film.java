package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class Film {
    private long id;
    @NotBlank
    private String name;
    @NotEmpty
    @Size(max = 200)
    private String description;
    @PastOrPresent
    private LocalDate releaseDate;
    @Positive
    private long duration;
    private MPA mpa;
    private Set<Genre> genres;
    private Set<Long> likes;
}
