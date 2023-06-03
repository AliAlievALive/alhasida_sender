package com.dashaval.frompast.sender;

import com.dashaval.frompast.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SenderRepositoryTest extends AbstractTestcontainer {
    @Autowired
    private SenderRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsSenderByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Sender sender = new Sender(
                FAKER.name().firstName(),
                email,
                20
        );
        underTest.save(sender);

        // When
        var actual = underTest.existsSenderByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsSenderByEmailFailsWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        var actual = underTest.existsSenderByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }
}