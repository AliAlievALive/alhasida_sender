package com.alhasid.sender;

import com.alhasid.exception.DuplicateResourceException;
import com.alhasid.exception.RequestValidationException;
import com.alhasid.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SenderServiceTest {
    @Mock
    private SenderDao senderDao;
    private SenderService underTest;

    @BeforeEach
    void setUp() {
        underTest = new SenderService(senderDao);
    }

    @Test
    void getAllSenders() {
        // When
        underTest.getAllSenders();
        // Then
        verify(senderDao).selectAllSenders();
    }

    @Test
    void canGetSender() {
        // Given
        long id = 10;
        Sender sender = new Sender(id, "test@t.com");
        when(senderDao.selectSenderById(id)).thenReturn(Optional.of(sender));

        // When
        Sender actual = underTest.getSender(id);

        // Then
        assertThat(actual).isEqualTo(sender);
    }

    @Test
    void willThrowWhenGetSenderReturnEmptyOptional() {
        // Given
        long id = 10;
        when(senderDao.selectSenderById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getSender(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("sender with id [%s] not found".formatted(id));
    }

    @Test
    void addSender() {
        // Given
        String email = "ali@mail.ru";
        when(senderDao.existsSenderWithEmail(email)).thenReturn(false);
        SenderRegistrationRequest request = new SenderRegistrationRequest(email);

        // When
        underTest.addSender(request);

        // Then
        ArgumentCaptor<Sender> senderArgumentCaptor = ArgumentCaptor.forClass(Sender.class);

        verify(senderDao).insertSender(senderArgumentCaptor.capture());

        Sender capturedSender = senderArgumentCaptor.getValue();

        assertThat(capturedSender.getId()).isNull();
        assertThat(capturedSender.getEmail()).isEqualTo(request.email());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingSender() {
        // Given
        String email = "ali@mail.ru";
        when(senderDao.existsSenderWithEmail(email)).thenReturn(true);
        SenderRegistrationRequest request = new SenderRegistrationRequest(email);

        // When
        assertThatThrownBy(() -> underTest.addSender(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then

        verify(senderDao, never()).insertSender(any());
    }

    @Test
    void deleteSender() {
        // Given
        long senderId = 10;
        when(senderDao.existsSenderWithId(senderId)).thenReturn(true);

        // When
        underTest.deleteSender(senderId);

        // Then
        verify(senderDao).deleteSender(senderId);
    }

    @Test
    void willThrowDeleteSenderByIdNotExists() {
        // Given
        long senderId = 10;
        when(senderDao.existsSenderWithId(senderId)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteSender(senderId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("sender with id [%s] not found".formatted(senderId));

        // Then
        verify(senderDao, never()).deleteSender(senderId);
    }

    @Test
    void canUpdateOnlySenderEmail() {
        // Given
        long id = 10;
        Sender sender = new Sender(id, "test@t.com");
        when(senderDao.selectSenderById(id)).thenReturn(Optional.of(sender));
        String newEmail = "alih@mail.ru";
        SenderUpdateRequest updateRequest = new SenderUpdateRequest(newEmail);

        // When
        underTest.updateSender(id, updateRequest);

        // Then
        ArgumentCaptor<Sender> senderArgumentCaptor = ArgumentCaptor.forClass(Sender.class);
        verify(senderDao).updateSender(senderArgumentCaptor.capture());
        Sender capturedSender = senderArgumentCaptor.getValue();

        assertThat(capturedSender.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void willThrowWhenTryingUpdateSenderEmailWhenAlreadyTaken() {
        // Given
        long id = 10;
        Sender sender = new Sender(id, "test@t.com");
        when(senderDao.selectSenderById(id)).thenReturn(Optional.of(sender));
        String newEmail = "alih@mail.ru";
        SenderUpdateRequest updateRequest = new SenderUpdateRequest(newEmail);

        when(senderDao.existsSenderWithEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateSender(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(senderDao, never()).updateSender(any());
    }

    @Test
    void willThrowWhenUpdateSenderHasNotChanges() {
        // Given
        long id = 10;
        Sender sender = new Sender(id, "test@t.com");
        when(senderDao.selectSenderById(id)).thenReturn(Optional.of(sender));
        SenderUpdateRequest updateRequest = new SenderUpdateRequest(sender.getEmail());

        // When
        assertThatThrownBy(() -> underTest.updateSender(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then

        verify(senderDao, never()).updateSender(any());
    }
}