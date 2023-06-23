package com.alhasid.taker;

import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@AllArgsConstructor
public class TakerRowMapper implements RowMapper<Taker> {
    private final SenderRepository senderRepository;

    @Override
    public Taker mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sender sender = senderRepository.findById(rs.getLong("sender_id")).orElse(new Sender(0, "", ""));
        return new Taker(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age"),
                Gender.valueOf(rs.getString("gender")),
                sender
        );
    }
}
