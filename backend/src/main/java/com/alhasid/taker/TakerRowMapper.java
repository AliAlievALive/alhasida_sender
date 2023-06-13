package com.alhasid.taker;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TakerRowMapper implements RowMapper<Taker> {
    @Override
    public Taker mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Taker(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age")
        );
    }
}
