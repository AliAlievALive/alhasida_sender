package com.alhasid.sender;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SenderRowMapper implements RowMapper<Sender> {
    @Override
    public Sender mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Sender(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age")
        );
    }
}
