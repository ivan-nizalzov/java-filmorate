package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private long id;
    private String name;
    @Email
    @NotEmpty
    private String email;
    @NotEmpty(message = "Логин не может содержать пробелы.")
    @NotNull(message = "Логин не может быть пустым")
    private String login;
    @Past
    private LocalDate birthday;
}