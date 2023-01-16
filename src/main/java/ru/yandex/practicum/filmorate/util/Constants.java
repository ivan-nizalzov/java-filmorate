package ru.yandex.practicum.filmorate.util;

import java.time.LocalDate;

public class Constants {

    private Constants() {
    }

    public static final LocalDate EARLIEST_DATE_OF_RELEASE = LocalDate.of(1895, 12, 28);
    public static final int MAX_LENGTH_OF_DESCRIPTION = 200;

    public static final String TOO_LONG_DESCRIPTION = "В этом описании более 200 символов, по этой причине создание " +
            "объекта с полем film.description не пройдет валидацию контроллера. В поле необходимо указать описание " +
            "фильма, не превышающим количество символов, равное 200.";
}
