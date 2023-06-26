package com.alhasid.journey;

import com.alhasid.auth.AuthenticationRequest;
import com.alhasid.auth.AuthenticationResponse;
import com.alhasid.jwt.JWTUtil;
import com.alhasid.sender.SenderDTO;
import com.alhasid.sender.SenderRegistrationRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AuthenticationIT {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JWTUtil jwtUtil;
    private static final String AUTHENTICATION_PATH = "/api/v1/auth";
    private static final String SENDER_PATH = "/api/v1/senders";

    @Test
    void canLogin() {
        // Given
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
        String password = "pass";
        SenderRegistrationRequest senderRegistrationRequest = new SenderRegistrationRequest(email, password);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);
        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        // send a post request
        webTestClient.post()
                .uri(SENDER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(senderRegistrationRequest), SenderRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

        String jwtToken = Objects.requireNonNull(result
                        .getResponseHeaders()
                        .get(AUTHORIZATION))
                .get(0);
        AuthenticationResponse authenticationResponse = result.getResponseBody();
        assert authenticationResponse != null;
        SenderDTO senderDTO = authenticationResponse.senderDTO();
        assertThat(jwtUtil.isTokenValid(
                jwtToken,
                senderDTO.username())
        ).isTrue();
        assertThat(senderDTO.email()).isEqualTo(email);
        assertThat(senderDTO.username()).isEqualTo(email);
        assertThat(senderDTO.roles()).isEqualTo(List.of("ROLE_USER"));
        assertThat(senderDTO.takers()).isEqualTo(List.of());
    }
}
