package com.alhasid.taker;

import com.alhasid.AbstractTakerTestcontainer;
import com.alhasid.sender.Sender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TakerRepositoryTest extends AbstractTakerTestcontainer {
    @Autowired
    private TakerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsTakerByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Sender sender = new Sender("test@t.com", "pass");
        Taker taker = new Taker(
                FAKER.name().firstName(),
                email,
                20,
                Gender.MALE,
                sender);
        underTest.save(taker);

        // When
        var actual = underTest.existsTakerByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsTakerByEmailFailsWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        var actual = underTest.existsTakerByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }
}