package com.alhasid.sender;

import com.alhasid.exception.DuplicateResourceException;
import com.alhasid.exception.RequestValidationException;
import com.alhasid.exception.ResourceNotFoundException;
import com.alhasid.taker.Taker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SenderServiceTest {
    @Mock
    private SenderDao senderDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private SenderService underTest;

    @BeforeEach
    void setUp() {
        underTest = new SenderService(senderDao, passwordEncoder);
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
        Sender sender = new Sender(id, "test@t.com", "pass");
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
        SenderRegistrationRequest request = new SenderRegistrationRequest(email, "pass");
        String passwordHash = "dsfwfe23rfr46_";
        when(passwordEncoder.encode("pass")).thenReturn(passwordHash);

        // When
        underTest.addSender(request);

        // Then
        ArgumentCaptor<Sender> senderArgumentCaptor = ArgumentCaptor.forClass(Sender.class);

        verify(senderDao).insertSender(senderArgumentCaptor.capture());

        Sender capturedSender = senderArgumentCaptor.getValue();

        assertThat(capturedSender.getId()).isNull();
        assertThat(capturedSender.getEmail()).isEqualTo(request.email());
        assertThat(capturedSender.getPassword()).isEqualTo(passwordHash);
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingSender() {
        // Given
        String email = "ali@mail.ru";
        when(senderDao.existsSenderWithEmail(email)).thenReturn(true);
        SenderRegistrationRequest request = new SenderRegistrationRequest(email, "pass");

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
        Sender sender = new Sender(id, "test@t.com", "pass");
        when(senderDao.selectSenderById(id)).thenReturn(Optional.of(sender));
        String newEmail = "alih@mail.ru";
        when(passwordEncoder.encode("pass")).thenReturn("pass");
        SenderUpdateRequest updateRequest = new SenderUpdateRequest(newEmail, sender.getPassword());

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
        Sender sender = new Sender(id, "test@t.com", "pass");
        when(senderDao.selectSenderById(id)).thenReturn(Optional.of(sender));
        String newEmail = "alih@mail.ru";
        String passwordHash = "dsfwfe23rfr46_";
        SenderUpdateRequest updateRequest = new SenderUpdateRequest(newEmail, passwordHash);

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
        Sender sender = new Sender(id, "test@t.com", "pass", List.of(new Taker()));
        when(senderDao.selectSenderById(id)).thenReturn(Optional.of(sender));
        when(passwordEncoder.encode("pass")).thenReturn("pass");
        SenderUpdateRequest updateRequest = new SenderUpdateRequest(sender.getEmail(), sender.getPassword());

        // When
        assertThatThrownBy(() -> underTest.updateSender(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then

        verify(senderDao, never()).updateSender(any());
    }

    @Test
    void canUpdateOnlySenderPassword() {
        // Given
        long id = 10;
        String email = "test@t.com";
        Sender sender = new Sender(id, email, "pass");
        when(senderDao.selectSenderById(id)).thenReturn(Optional.of(sender));
        String newPassword = "PassNew";
        String passwordHash = "dsfwfe23rfr46_";
        when(passwordEncoder.encode(newPassword)).thenReturn(passwordHash);
        SenderUpdateRequest updateRequest = new SenderUpdateRequest(email, newPassword);

        // When
        underTest.updateSender(id, updateRequest);

        // Then
        ArgumentCaptor<Sender> senderArgumentCaptor = ArgumentCaptor.forClass(Sender.class);
        verify(senderDao).updateSender(senderArgumentCaptor.capture());
        Sender capturedSender = senderArgumentCaptor.getValue();

        assertThat(capturedSender.getEmail()).isEqualTo(email);
        assertThat(capturedSender.getPassword()).isEqualTo(passwordEncoder.encode(newPassword));
    }
}