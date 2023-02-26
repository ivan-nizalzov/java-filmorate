package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    private MPA makeMpa(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("MPA_ID");
        String name = rs.getString("NAME");

        return MPA.builder().
                id(id).
                name(name).
                build();
    }

    @Override
    public Collection<MPA> findAll() {
        final String sqlQuery = "SELECT * FROM MPA";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    @Override
    public MPA findById(Long mpaId) throws NotFoundException {
        final String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
        List<MPA> mpas = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs, rowNum), mpaId);
        if (mpas.size() < 1) {
            throw new NotFoundException("mpa", mpaId);
        }
        return mpas.get(0);
    }

    @Override
    public String findNameById(Long mpaId) throws NotFoundException {
        final String sqlQuery = "SELECT NAME FROM MPA WHERE MPA_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, mpaId);

        if (rs.next()) {
            return rs.getString("NAME");
        }
        throw new NotFoundException("mpa", mpaId);
    }
}