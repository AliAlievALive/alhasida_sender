package com.alhasid.taker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class TakerJDBCDataAccessService implements TakerDao {
    private final JdbcTemplate jdbcTemplate;
    private final TakerRowMapper takerRowMapper;

    public TakerJDBCDataAccessService(JdbcTemplate jdbcTemplate, TakerRowMapper takerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.takerRowMapper = takerRowMapper;
    }

    @Override
    public List<Taker> selectAllTakers() {
        var sql = """
                SELECT id, name, email, age, gender, sender_id
                FROM taker
                """;

        return jdbcTemplate.query(sql, takerRowMapper);
    }

    @Override
    public Optional<Taker> selectTakerById(Long id) {
        var sql = """
                SELECT id, name, email, age, gender, sender_id
                FROM taker
                WHERE id = ?""";
        return jdbcTemplate.query(sql, takerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertTaker(Taker taker) {
        var sql = """
                INSERT INTO taker(name, email, age, gender)
                VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, taker.getName(), taker.getEmail(), taker.getAge(), taker.getGender().name());
    }

    @Override
    public boolean existsTakerWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM taker
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public void deleteTaker(Long id) {
        var sql = """
                DELETE
                FROM taker
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsTakerWithId(Long takerId) {
        var sql = """
                SELECT count(id)
                FROM taker
                WHERE id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, takerId);
        return count != null && count > 0;
    }

    @Override
    public void updateTaker(Taker update) {
        if (update.getName() != null) {
            var sql = "UPDATE taker SET name = ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getName(), update.getId());
        }

        if (update.getEmail() != null) {
            var sql = "UPDATE taker SET email = ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getEmail(), update.getId());
        }

        if (update.getAge() != null) {
            var sql = "UPDATE taker SET age = ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getAge(), update.getId());
        }
    }

    @Override
    public List<Taker> selectTakersForSender(Long id) {
        var sql = """
                SELECT id, name, email, age, gender, sender_id
                FROM taker
                WHERE sender_id = ?""";
        return jdbcTemplate.query(sql, takerRowMapper, id);
    }
}
