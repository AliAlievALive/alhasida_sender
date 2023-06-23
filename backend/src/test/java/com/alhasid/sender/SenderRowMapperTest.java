package com.alhasid.sender;

import com.alhasid.taker.Gender;
import com.alhasid.taker.Taker;
import com.alhasid.taker.TakerRepository;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SenderRowMapperTest {
    private final TakerRepository takerRepository = mock(TakerRepository.class);

    @Test
    void mapRow() throws SQLException {
        // Given
        Sender sender = new Sender();
        Taker taker = new Taker(1L, "Ali", "ali@test.ru", 20, Gender.MALE, sender);
        when(takerRepository.getTakersBySenderId(1L)).thenReturn(List.of(taker));
        SenderRowMapper senderRowMapper = new SenderRowMapper(takerRepository);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("email")).thenReturn("ali@test.ru");

        // When
        Sender actual = senderRowMapper.mapRow(resultSet, 1);

        // Then
        Sender expected = new Sender(1L, "ali@test.ru", "pass", List.of(taker));
        assertThat(actual).isEqualTo(expected);
    }
}