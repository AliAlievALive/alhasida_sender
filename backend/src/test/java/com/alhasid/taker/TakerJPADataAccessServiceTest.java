package com.alhasid.taker;

import com.alhasid.sender.Sender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class TakerJPADataAccessServiceTest {
    private TakerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private TakerRepository takerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new TakerJPADataAccessService(takerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllTakers() {
        // When
        underTest.selectAllTakers();

        // Then
        verify(takerRepository).findAll();
    }

    @Test
    void selectTakerById() {
        // Given
        long id = 1;

        // When
        underTest.selectTakerById(id);

        // Then
        verify(takerRepository).findById(id);
    }

    @Test
    void insertTaker() {
        // Given
        Taker taker = new Taker("Ali", "ali@mail.ru", 20, Gender.MALE, new Sender("test@t.com", "pass"));

        // When
        underTest.insertTaker(taker);

        // Then
        verify(takerRepository).save(taker);
    }

    @Test
    void existsTakerWithEmail() {
        // Given
        String email = "ali@mail.ru";

        // When
        underTest.existsTakerWithEmail(email);

        // Then
        verify(takerRepository).existsTakerByEmail(email);
    }

    @Test
    void deleteTaker() {
        // Given
        long id = 1;

        // When
        underTest.deleteTaker(id);

        // Then
        verify(takerRepository).deleteById(id);
    }

    @Test
    void existsTakerWithId() {
        // Given
        long id = 1;

        // When
        underTest.existsTakerWithId(id);

        // Then
        verify(takerRepository).existsById(id);
    }

    @Test
    void updateTaker() {
        // Given
        Taker taker = new Taker("Ali", "ali@mail.ru", 20, Gender.MALE, new Sender("test@t.com", "pass"));

        // When
        underTest.updateTaker(taker);

        // Then
        verify(takerRepository).save(taker);
    }
}