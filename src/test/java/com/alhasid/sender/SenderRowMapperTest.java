package com.alhasid.sender;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SenderRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        SenderRowMapper senderRowMapper = new SenderRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getInt("age")).thenReturn(20);
        when(resultSet.getString("name")).thenReturn("Ali");
        when(resultSet.getString("email")).thenReturn("ali@test.ru");

        // When
        Sender actual = senderRowMapper.mapRow(resultSet, 1);

        // Then
        Sender expected = new Sender(1L, "Ali", "ali@test.ru", 20);
        assertThat(actual).isEqualTo(expected);
    }
}