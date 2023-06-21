package com.alhasid.taker;

import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderRepository;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TakerRowMapperTest {
    private final SenderRepository senderRepository = mock(SenderRepository.class);
    @Test
    void mapRow() throws SQLException {
        // Given
        TakerRowMapper takerRowMapper = new TakerRowMapper(senderRepository);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getInt("age")).thenReturn(20);
        when(resultSet.getString("name")).thenReturn("Ali");
        when(resultSet.getString("email")).thenReturn("ali@test.ru");
        when(resultSet.getString("gender")).thenReturn("MALE");
        Sender sender = new Sender("");
        sender.setId(resultSet.getLong("sender_id"));
        when(resultSet.getObject("sender_id")).thenReturn(sender);

        // When
        Taker actual = takerRowMapper.mapRow(resultSet, 1);

        // Then
        Taker expected = new Taker(1L, "Ali", "ali@test.ru", 20, Gender.MALE, sender);
        assertThat(actual).isEqualTo(expected);
    }
}