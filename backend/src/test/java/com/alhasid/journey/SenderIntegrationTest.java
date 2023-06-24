package com.alhasid.journey;

import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderDTO;
import com.alhasid.sender.SenderRegistrationRequest;
import com.alhasid.sender.SenderUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SenderIntegrationTest {
    private static final String SENDER_URI = "/api/v1/senders";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterSender() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
        String password = "pass";
        SenderRegistrationRequest request = new SenderRegistrationRequest(email, password);

        // send a post request
        String jwtToken = getJwtToken(request);

        // get all senders
        List<SenderDTO> allSenders = webTestClient.get()
                .uri(SENDER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<SenderDTO>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = null;
        if (allSenders != null) {
            id = allSenders.stream()
                    .filter(sender -> sender.email().equals(email))
                    .map(SenderDTO::id)
                    .findFirst()
                    .orElseThrow();
        }

        // make sure that sender is present
        SenderDTO expectedSender = new SenderDTO(id, email, List.of("ROLE_USER"), List.of(), email);

        assertThat(allSenders).contains(expectedSender);

        // get sender by id
        webTestClient.get()
                .uri(SENDER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<SenderDTO>() {
                })
                .isEqualTo(expectedSender);
    }

    @Test
    void canDeleteSender() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
        String password = "pass";

        SenderRegistrationRequest request = new SenderRegistrationRequest(email, password);

        // send a post request
        String jwtToken = getJwtToken(request);


        // get all senders
        List<SenderDTO> allSenders = webTestClient.get()
                .uri(SENDER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<SenderDTO>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = null;
        if (allSenders != null) {
            id = allSenders.stream()
                    .filter(sender -> sender.email().equals(email))
                    .map(SenderDTO::id)
                    .findFirst()
                    .orElseThrow();
        }

        // delete sender
        webTestClient.delete()
                .uri(SENDER_URI + "/{id}", id)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get sender by id
        webTestClient.get()
                .uri(SENDER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void canUpdateSender() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
        String password = "pass";

        SenderRegistrationRequest request = new SenderRegistrationRequest(email, password);

        // send a post request
        String jwtToken = getJwtToken(request);

        // get all senders
        List<Sender> allSenders = getResponseBody(jwtToken);

        Long id = null;
        id = getId(email, allSenders, id);

        // update sender
        String newEmail = UUID.randomUUID() + "newemail@mail.ru";
        SenderUpdateRequest updateRequest = new SenderUpdateRequest(newEmail, null);
        webTestClient.put()
                .uri(SENDER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .body(Mono.just(updateRequest), SenderUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        jwtToken = getJwtToken(request);

        allSenders = getResponseBody(jwtToken);

        id = getId(email, allSenders, id);

        // get sender by id
        SenderDTO updatedSender = webTestClient.get()
                .uri(SENDER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SenderDTO.class)
                .returnResult()
                .getResponseBody();

        SenderDTO expected = new SenderDTO(id, email, List.of("ROLE_USER"), List.of(), email);

        assertThat(updatedSender).isEqualTo(expected);
    }

    private static Long getId(String email, List<Sender> allSenders, Long id) {
        if (allSenders != null) {
            id = allSenders.stream()
                    .filter(sender -> sender.getEmail().equals(email))
                    .map(Sender::getId)
                    .findFirst()
                    .orElseThrow();
        }
        return id;
    }

    @Nullable
    private List<Sender> getResponseBody(String jwtToken) {
        return webTestClient.get()
                .uri(SENDER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Sender>() {
                })
                .returnResult()
                .getResponseBody();
    }

    private String getJwtToken(SenderRegistrationRequest request) {
        return Objects.requireNonNull(webTestClient.post()
                        .uri(SENDER_URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(request), SenderRegistrationRequest.class)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseHeaders()
                        .get(AUTHORIZATION))
                .get(0);
    }
}
