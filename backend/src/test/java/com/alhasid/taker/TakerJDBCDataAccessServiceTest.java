package com.alhasid.taker;

import com.alhasid.AbstractTakerTestcontainer;
import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class TakerJDBCDataAccessServiceTest extends AbstractTakerTestcontainer {
    private final SenderRepository senderRepository = mock(SenderRepository.class);
    private final TakerRowMapper takerRowMapper = new TakerRowMapper(senderRepository);
    private TakerJDBCDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TakerJDBCDataAccessService(
                getJdbcTemplate(),
                takerRowMapper
        );
    }

    @Test
    void selectAllTakers() {
        // Given
        Taker taker = new Taker(
                FAKER.name().firstName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);

        // When
        List<Taker> actual = underTest.selectAllTakers();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectTakerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Taker taker = new Taker(
                FAKER.name().firstName(),
                email,
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);
        Long id = underTest.selectAllTakers()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Taker::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Taker> actual = underTest.selectTakerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(taker.getName());
            assertThat(s.getEmail()).isEqualTo(taker.getEmail());
            assertThat(s.getAge()).isEqualTo(taker.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectTakerById() {
        // Given
        long id = 0;

        // When
        var actual = underTest.selectTakerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertTaker() {
        // Given
        int size = underTest.selectAllTakers().size();
        Taker taker = new Taker(
                FAKER.name().firstName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);

        // When
        int actual = underTest.selectAllTakers().size();

        // Then
        assertThat(actual).isEqualTo(size + 1);
    }

    @Test
    void existsTakerWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Taker taker = new Taker(
                name,
                email,
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);

        // When
        boolean actual = underTest.existsTakerWithEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsTakerWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsTakerWithEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteTaker() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Taker taker = new Taker(
                name,
                email,
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);
        Long id = underTest.selectAllTakers()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Taker::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteTaker(id);

        // Then
        assertThat(underTest.selectTakerById(id)).isNotPresent();
    }

    @Test
    void existsTakerWithId() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Taker taker = new Taker(
                name,
                email,
                20,
                Gender.MALE, new Sender("test@t.com"));
        underTest.insertTaker(taker);
        Long id = underTest.selectAllTakers()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Taker::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsTakerWithId(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsTakerWithIdReturnsFalseWhenDoesNotExists() {
        // Given
        long id = 0;

        // When
        boolean actual = underTest.existsTakerWithId(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateTakerName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Taker taker = new Taker(
                name,
                email,
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);
        Long id = underTest.selectAllTakers()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Taker::getId)
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        // When
        Taker update = new Taker();
        update.setId(id);
        update.setName(newName);

        underTest.updateTaker(update);

        // Then
        assertThat(underTest.selectTakerById(id)).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(newName);
            assertThat(s.getEmail()).isEqualTo(email);
            assertThat(s.getAge()).isEqualTo(taker.getAge());
        });
    }

    @Test
    void updateTakerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Taker taker = new Taker(
                name,
                email,
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);
        Long id = underTest.selectAllTakers()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Taker::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        Taker update = new Taker();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateTaker(update);

        // Then
        assertThat(underTest.selectTakerById(id)).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(name);
            assertThat(s.getEmail()).isEqualTo(newEmail);
            assertThat(s.getAge()).isEqualTo(taker.getAge());
        });
    }

    @Test
    void updateTakerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Taker taker = new Taker(
                name,
                email,
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);
        Long id = underTest.selectAllTakers()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Taker::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 100;

        // When
        Taker update = new Taker();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateTaker(update);

        // Then
        assertThat(underTest.selectTakerById(id)).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(name);
            assertThat(s.getEmail()).isEqualTo(email);
            assertThat(s.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willUpdateAllPropertiesTaker() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Taker taker = new Taker(
                name,
                email,
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);
        Long id = underTest.selectAllTakers()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Taker::getId)
                .findFirst()
                .orElseThrow();

        int newAge = 100;

        // When
        Taker update = new Taker();
        update.setId(id);
        update.setAge(newAge);
        String newEmail = UUID.randomUUID().toString();
        update.setEmail(newEmail);
        update.setName("foo");

        underTest.updateTaker(update);

        // Then
        assertThat(underTest.selectTakerById(id)).isPresent().hasValueSatisfying(updated ->{
            assertThat(updated.getId()).isEqualTo(id);
            assertThat(updated.getGender()).isEqualTo(Gender.MALE);
            assertThat(updated.getName()).isEqualTo("foo");
            assertThat(updated.getEmail()).isEqualTo(newEmail);
            assertThat(updated.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willNotUpdateAllPropertiesTaker() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().firstName();
        Taker taker = new Taker(
                name,
                email,
                20,
                Gender.MALE,
                new Sender("test@t.com")
        );
        underTest.insertTaker(taker);
        Long id = underTest.selectAllTakers()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(Taker::getId)
                .findFirst()
                .orElseThrow();

        // When
        Taker update = new Taker();
        update.setId(id);

        underTest.updateTaker(update);

        // Then
        assertThat(underTest.selectTakerById(id)).isPresent().hasValueSatisfying(s -> {
            assertThat(s.getId()).isEqualTo(id);
            assertThat(s.getName()).isEqualTo(taker.getName());
            assertThat(s.getEmail()).isEqualTo(taker.getEmail());
            assertThat(s.getAge()).isEqualTo(taker.getAge());
        });
    }
}