package com.alhasid.sender;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class SenderJPADataAccessServiceTest {
    private SenderJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private SenderRepository senderRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new SenderJPADataAccessService(senderRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllSenders() {
        // When
        underTest.selectAllSenders();

        // Then
        verify(senderRepository).findAll();
    }

    @Test
    void selectSenderById() {
        // Given
        long id = 1;

        // When
        underTest.selectSenderById(id);

        // Then
        verify(senderRepository).findById(id);
    }

    @Test
    void insertSender() {
        // Given
        Sender sender = new Sender("Ali", "ali@mail.ru", 20);

        // When
        underTest.insertSender(sender);

        // Then
        verify(senderRepository).save(sender);
    }

    @Test
    void existsSenderWithEmail() {
        // Given
        String email = "ali@mail.ru";

        // When
        underTest.existsSenderWithEmail(email);

        // Then
        verify(senderRepository).existsSenderByEmail(email);
    }

    @Test
    void deleteSender() {
        // Given
        long id = 1;

        // When
        underTest.deleteSender(id);

        // Then
        verify(senderRepository).deleteById(id);
    }

    @Test
    void existsSenderWithId() {
        // Given
        long id = 1;

        // When
        underTest.existsSenderWithId(id);

        // Then
        verify(senderRepository).existsById(id);
    }

    @Test
    void updateSender() {
        // Given
        Sender sender = new Sender("Ali", "ali@mail.ru", 20);

        // When
        underTest.updateSender(sender);

        // Then
        verify(senderRepository).save(sender);
    }
}