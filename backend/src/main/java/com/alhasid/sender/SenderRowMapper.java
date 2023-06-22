package com.alhasid.sender;

import com.alhasid.taker.Taker;
import com.alhasid.taker.TakerRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class SenderRowMapper implements RowMapper<Sender> {
    private final TakerRepository repository;

    public SenderRowMapper(TakerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Sender mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sender sender = new Sender(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("pass"));
        List<Taker> takers = repository.getTakersBySenderId(sender.getId());
        sender.setTakers(takers);
        return sender;
    }
}
