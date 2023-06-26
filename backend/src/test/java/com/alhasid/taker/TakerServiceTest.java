package com.alhasid.taker;

import com.alhasid.exception.DuplicateResourceException;
import com.alhasid.exception.RequestValidationException;
import com.alhasid.exception.ResourceNotFoundException;
import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TakerServiceTest {
    @Mock
    private TakerDao takerDao;

    @Mock
    private SenderDao senderDao;
    private TakerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TakerService(takerDao, senderDao);
    }

    @Test
    void getAllTakers() {
        // When
        underTest.getAllTakers();
        // Then
        verify(takerDao).selectAllTakers();
    }

    @Test
    void canGetTaker() {
        // Given
        long id = 10;
        Taker taker = new Taker(id, "Ali", "ali@test.com", 20, Gender.MALE, new Sender("test@t.com", "pass"));
        when(takerDao.selectTakerById(id)).thenReturn(Optional.of(taker));

        // When
        Taker actual = underTest.getTaker(id);

        // Then
        assertThat(actual).isEqualTo(taker);
    }

    @Test
    void willThrowWhenGetTakerReturnEmptyOptional() {
        // Given
        long id = 10;
        when(takerDao.selectTakerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getTaker(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("taker with id [%s] not found".formatted(id));
    }

    @Test
    void addTaker() {
        // Given
        String email = "ali@mail.ru";
        String password = "pass";
        when(senderDao.selectSenderById(70L)).thenReturn(Optional.of(new Sender(email, password, List.of())));
        TakerRegistrationRequest request = new TakerRegistrationRequest("Ali", email, 20, Gender.MALE, 70L);

        // When
        underTest.addTaker(request);

        // Then
        ArgumentCaptor<Taker> takerArgumentCaptor = ArgumentCaptor.forClass(Taker.class);

        verify(takerDao).insertTaker(takerArgumentCaptor.capture());

        Taker capturedTaker = takerArgumentCaptor.getValue();

        assertThat(capturedTaker.getId()).isNull();
        assertThat(capturedTaker.getName()).isEqualTo(request.name());
        assertThat(capturedTaker.getEmail()).isEqualTo(request.email());
        assertThat(capturedTaker.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingTaker() {
        // Given
        String email = "ali@mail.ru";
        String password = "pass";
        Sender sender = new Sender(email, password, List.of(new Taker("testname", email, 23, Gender.MALE)));
        when(senderDao.selectSenderById(70L)).thenReturn(Optional.of(sender));
        TakerRegistrationRequest request = new TakerRegistrationRequest("Ali", email, 20, Gender.MALE, 70L);

        // When
        assertThatThrownBy(() -> underTest.addTaker(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then

        verify(takerDao, never()).insertTaker(any());
    }

    @Test
    void deleteTaker() {
        // Given
        long takerId = 10;
        when(takerDao.existsTakerWithId(takerId)).thenReturn(true);

        // When
        underTest.deleteTaker(takerId);

        // Then
        verify(takerDao).deleteTaker(takerId);
    }

    @Test
    void willThrowDeleteTakerByIdNotExists() {
        // Given
        long takerId = 10;
        when(takerDao.existsTakerWithId(takerId)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteTaker(takerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("taker with id [%s] not found".formatted(takerId));

        // Then
        verify(takerDao, never()).deleteTaker(takerId);
    }

    @Test
    void canUpdateTakerAllProperties() {
        // Given
        long id = 10;
        Taker taker = new Taker(id, "Ali", "ali@test.com", 20, Gender.MALE, new Sender("test@t.com", "pass"));
        when(takerDao.selectTakerById(id)).thenReturn(Optional.of(taker));
        String newEmail = "alih@mail.ru";
        TakerUpdateRequest updateRequest = new TakerUpdateRequest("Alihandro", newEmail, 22);

        when(takerDao.existsTakerWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateTaker(id, updateRequest);

        // Then
        ArgumentCaptor<Taker> takerArgumentCaptor = ArgumentCaptor.forClass(Taker.class);
        verify(takerDao).updateTaker(takerArgumentCaptor.capture());
        Taker capturedTaker = takerArgumentCaptor.getValue();

        assertThat(capturedTaker.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedTaker.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedTaker.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void canUpdateOnlyTakerName() {
        // Given
        long id = 10;
        Taker taker = new Taker(id, "Ali", "ali@test.com", 20, Gender.MALE, new Sender("test@t.com", "pass"));
        when(takerDao.selectTakerById(id)).thenReturn(Optional.of(taker));
        TakerUpdateRequest updateRequest = new TakerUpdateRequest("Alihandro", null, null);

        // When
        underTest.updateTaker(id, updateRequest);

        // Then
        ArgumentCaptor<Taker> takerArgumentCaptor = ArgumentCaptor.forClass(Taker.class);
        verify(takerDao).updateTaker(takerArgumentCaptor.capture());
        Taker capturedTaker = takerArgumentCaptor.getValue();

        assertThat(capturedTaker.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedTaker.getAge()).isEqualTo(taker.getAge());
        assertThat(capturedTaker.getEmail()).isEqualTo(taker.getEmail());
    }

    @Test
    void canUpdateOnlyTakerEmail() {
        // Given
        long id = 10;
        Taker taker = new Taker(id, "Ali", "ali@test.com", 20, Gender.MALE, new Sender("test@t.com", "pass"));
        when(takerDao.selectTakerById(id)).thenReturn(Optional.of(taker));
        String newEmail = "alih@mail.ru";
        TakerUpdateRequest updateRequest = new TakerUpdateRequest(null, newEmail, null);

        // When
        underTest.updateTaker(id, updateRequest);

        // Then
        ArgumentCaptor<Taker> takerArgumentCaptor = ArgumentCaptor.forClass(Taker.class);
        verify(takerDao).updateTaker(takerArgumentCaptor.capture());
        Taker capturedTaker = takerArgumentCaptor.getValue();

        assertThat(capturedTaker.getName()).isEqualTo(taker.getName());
        assertThat(capturedTaker.getAge()).isEqualTo(taker.getAge());
        assertThat(capturedTaker.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void canUpdateOnlyTakerAge() {
        // Given
        long id = 10;
        Taker taker = new Taker(id, "Ali", "ali@test.com", 20, Gender.MALE, new Sender("test@t.com", "pass"));
        when(takerDao.selectTakerById(id)).thenReturn(Optional.of(taker));
        TakerUpdateRequest updateRequest = new TakerUpdateRequest(null, null, 35);

        // When
        underTest.updateTaker(id, updateRequest);

        // Then
        ArgumentCaptor<Taker> takerArgumentCaptor = ArgumentCaptor.forClass(Taker.class);
        verify(takerDao).updateTaker(takerArgumentCaptor.capture());
        Taker capturedTaker = takerArgumentCaptor.getValue();

        assertThat(capturedTaker.getName()).isEqualTo(taker.getName());
        assertThat(capturedTaker.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedTaker.getEmail()).isEqualTo(taker.getEmail());
    }

    @Test
    void willThrowWhenTryingUpdateTakerEmailWhenAlreadyTaken() {
        // Given
        long id = 10;
        Taker taker = new Taker(id, "Ali", "ali@test.com", 20, Gender.MALE, new Sender("test@t.com", "pass"));
        when(takerDao.selectTakerById(id)).thenReturn(Optional.of(taker));
        String newEmail = "alih@mail.ru";
        TakerUpdateRequest updateRequest = new TakerUpdateRequest("Alihandro", newEmail, 22);

        when(takerDao.existsTakerWithEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateTaker(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(takerDao, never()).updateTaker(any());
    }

    @Test
    void willThrowWhenUpdateTakerHasNotChanges() {
        // Given
        long id = 10;
        Taker taker = new Taker(id, "Ali", "ali@test.com", 20, Gender.MALE, new Sender("test@t.com", "pass"));
        when(takerDao.selectTakerById(id)).thenReturn(Optional.of(taker));
        TakerUpdateRequest updateRequest = new TakerUpdateRequest(
                taker.getName(),
                taker.getEmail(),
                taker.getAge()
        );

        // When
        assertThatThrownBy(() -> underTest.updateTaker(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then

        verify(takerDao, never()).updateTaker(any());
    }
}