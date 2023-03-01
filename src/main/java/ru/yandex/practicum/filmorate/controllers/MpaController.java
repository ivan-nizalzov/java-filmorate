package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public Collection<MPA> findAll() {
        log.info("Запрос на получение всех MPA");
        return mpaService.findAll();
    }

    @GetMapping("{id}")
    public MPA findById(@PathVariable("id") Long id) throws NotFoundException {
        log.info("Запрос на получение MPA по id {}", id);
        return mpaService.findById(id);
    }
}