package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDbStorage implements MpaDbStorageInterface {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Mpa> mpaRowMapper = (resultSet, rowNum) -> {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("id"));
        mpa.setName(resultSet.getString("name"));
        return mpa;
    };
    private List<Mpa> mpas;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpas() {
        if (mpas == null) {
            fillMpas();
        }
        return mpas;
    }

    @Override
    public boolean hasMpaId(Long id) {
        if (mpas == null) {
            fillMpas();
        }
        for (Mpa mpa : mpas) {
            if (mpa.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void fillMpas() {
        String sql = "SELECT * FROM mpas";
        mpas = jdbcTemplate.query(sql, mpaRowMapper);
    }

    @Override
    public Mpa getMpaById(Long id) {
        if (mpas == null) {
            fillMpas();
        }
        for (Mpa mpa : mpas) {
            if (mpa.getId() == id)
                return mpa;
        }
        return null;
    }

}
