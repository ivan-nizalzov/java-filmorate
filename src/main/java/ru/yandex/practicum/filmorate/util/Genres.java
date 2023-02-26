package ru.yandex.practicum.filmorate.util;

public enum Genres {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    CARTOON("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик");

    private final String title;

    Genres(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
