package com.dashaval.frompast.sender;

import com.dashaval.frompast.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SenderJDBCDataAccessServiceTest extends AbstractTestcontainer {
    private final SenderRowMapper senderRowMapper = new SenderRowMapper();
    private SenderJDBCDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new SenderJDBCDataAccessService(
                getJdbcTemplate(),
                senderRowMapper
        );
    }

    @Test
    void selectAllSenders() {
        // Given
        Sender sender = new Sender(
                FAKER.name().firstName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertSender(sender);

        // When
        List<Sender> actual = underTest.selectAllSenders();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectSenderById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Sender sender = new Sender(
                FAKER.name().firstName(),
                email,
                20
        );
        underTest.insertSender(sender);
        Long id = underTest.selectAllSenders()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Sender::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Sender> actual = underTest.selectSenderById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(sender.getName());
            assertThat(s.getEmail()).isEqualTo(sender.getEmail());
            assertThat(s.getAge()).isEqualTo(sender.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectSenderById() {
        // Given
        long id = 0;

        // When
        var actual = underTest.selectSenderById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertSender() {
        // Given
        int size = underTest.selectAllSenders().size();
        Sender sender = new Sender(
                FAKER.name().firstName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertSender(sender);

        // When
        int actual = underTest.selectAllSenders().size();

        // Then
        assertThat(actual).isEqualTo(size + 1);
    }

    @Test
    void existsSenderWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Sender sender = new Sender(
                name,
                email,
                20
        );
        underTest.insertSender(sender);

        // When
        boolean actual = underTest.existsSenderWithEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsSenderWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsSenderWithEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteSender() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Sender sender = new Sender(
                name,
                email,
                20
        );
        underTest.insertSender(sender);
        Long id = underTest.selectAllSenders()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Sender::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteSender(id);

        // Then
        assertThat(underTest.selectSenderById(id)).isNotPresent();
    }

    @Test
    void existsSenderWithId() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Sender sender = new Sender(
                name,
                email,
                20
        );
        underTest.insertSender(sender);
        Long id = underTest.selectAllSenders()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Sender::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsSenderWithId(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsSenderWithIdReturnsFalseWhenDoesNotExists() {
        // Given
        long id = 0;

        // When
        boolean actual = underTest.existsSenderWithId(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateSenderName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Sender sender = new Sender(
                name,
                email,
                20
        );
        underTest.insertSender(sender);
        Long id = underTest.selectAllSenders()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Sender::getId)
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        // When
        Sender update = new Sender();
        update.setId(id);
        update.setName(newName);

        underTest.updateSender(update);

        // Then
        assertThat(underTest.selectSenderById(id)).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(newName);
            assertThat(s.getEmail()).isEqualTo(email);
            assertThat(s.getAge()).isEqualTo(sender.getAge());
        });
    }

    @Test
    void updateSenderEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Sender sender = new Sender(
                name,
                email,
                20
        );
        underTest.insertSender(sender);
        Long id = underTest.selectAllSenders()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Sender::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        Sender update = new Sender();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateSender(update);

        // Then
        assertThat(underTest.selectSenderById(id)).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(name);
            assertThat(s.getEmail()).isEqualTo(newEmail);
            assertThat(s.getAge()).isEqualTo(sender.getAge());
        });
    }

    @Test
    void updateSenderAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Sender sender = new Sender(
                name,
                email,
                20
        );
        underTest.insertSender(sender);
        Long id = underTest.selectAllSenders()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Sender::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 100;

        // When
        Sender update = new Sender();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateSender(update);

        // Then
        assertThat(underTest.selectSenderById(id)).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(name);
            assertThat(s.getEmail()).isEqualTo(email);
            assertThat(s.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willUpdateAllPropertiesSender() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Sender sender = new Sender(
                name,
                email,
                20
        );
        underTest.insertSender(sender);
        Long id = underTest.selectAllSenders()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Sender::getId)
                .findFirst()
                .orElseThrow();

        int newAge = 100;

        // When
        Sender update = new Sender();
        update.setId(id);
        update.setAge(newAge);
        update.setEmail(UUID.randomUUID().toString());
        update.setName("foo");

        underTest.updateSender(update);

        // Then
        assertThat(underTest.selectSenderById(id)).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateAllPropertiesSender() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Sender sender = new Sender(
                name,
                email,
                20
        );
        underTest.insertSender(sender);
        Long id = underTest.selectAllSenders()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Sender::getId)
                .findFirst()
                .orElseThrow();

        // When
        Sender update = new Sender();
        update.setId(id);

        underTest.updateSender(update);

        // Then
        assertThat(underTest.selectSenderById(id)).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(sender.getName());
            assertThat(s.getEmail()).isEqualTo(sender.getEmail());
            assertThat(s.getAge()).isEqualTo(sender.getAge());
        });
    }
}