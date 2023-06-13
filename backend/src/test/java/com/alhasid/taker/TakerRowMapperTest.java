package com.alhasid.taker;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TakerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        TakerRowMapper takerRowMapper = new TakerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getInt("age")).thenReturn(20);
        when(resultSet.getString("name")).thenReturn("Ali");
        when(resultSet.getString("email")).thenReturn("ali@test.ru");
        when(resultSet.getString("gender")).thenReturn("MALE");

        // When
        Taker actual = takerRowMapper.mapRow(resultSet, 1);

        // Then
        Taker expected = new Taker(1L, "Ali", "ali@test.ru", 20, Gender.MALE);
        assertThat(actual).isEqualTo(expected);
    }
}