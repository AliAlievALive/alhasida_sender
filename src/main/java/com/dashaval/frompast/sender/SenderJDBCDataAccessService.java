package com.dashaval.frompast.sender;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class SenderJDBCDataAccessService implements SenderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SenderRowMapper senderRowMapper;

    public SenderJDBCDataAccessService(JdbcTemplate jdbcTemplate, SenderRowMapper senderRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.senderRowMapper = senderRowMapper;
    }

    @Override
    public List<Sender> selectAllSenders() {
        var sql = """
                SELECT id, name, email, age
                FROM sender
                """;

        return jdbcTemplate.query(sql, senderRowMapper);
    }

    @Override
    public Optional<Sender> selectSenderById(Long id) {
        var sql = """
                SELECT id, name, email, age
                FROM sender
                WHERE id = ?""";
        return jdbcTemplate.query(sql, senderRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertSender(Sender sender) {
        var sql = """
                INSERT INTO sender(name, email, age)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sql, sender.getName(), sender.getEmail(), sender.getAge());
    }

    @Override
    public boolean existsSenderWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM sender
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public void deleteSender(Long id) {
        var sql = """
                DELETE
                FROM sender
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsSenderWithId(Long senderId) {
        var sql = """
                SELECT count(id)
                FROM sender
                WHERE id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, senderId);
        return count != null && count > 0;
    }

    @Override
    public void updateSender(Sender update) {
        if (update.getName() != null) {
            var sql = "UPDATE sender SET name = ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getName(), update.getId());
        }

        if (update.getEmail() != null) {
            var sql = "UPDATE sender SET email = ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getEmail(), update.getId());
        }

        if (update.getAge() != null) {
            var sql = "UPDATE sender SET age = ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getAge(), update.getId());
        }
    }
}
