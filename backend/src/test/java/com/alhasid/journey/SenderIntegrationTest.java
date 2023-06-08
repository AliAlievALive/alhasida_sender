package com.alhasid.journey;

import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderRegistrationRequest;
import com.alhasid.sender.SenderUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SenderIntegrationTest {
    private static final Random RANDOM = new Random();
    private static final String SENDER_URI = "/api/v1/senders";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterSender() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
        int age = RANDOM.nextInt(1, 100);

        SenderRegistrationRequest request = new SenderRegistrationRequest(name, email, age);

        // send a post request
        webTestClient.post()
                .uri(SENDER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), SenderRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all senders
        List<Sender> allSenders = webTestClient.get()
                .uri(SENDER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Sender>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that sender is present
        Sender expectedSender = new Sender(name, email, age);

        assertThat(allSenders)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedSender);

        Long id = null;
        if (allSenders != null) {
            id = allSenders.stream()
                    .filter(sender -> sender.getEmail().equals(email))
                    .map(Sender::getId)
                    .findFirst()
                    .orElseThrow();
        }

        expectedSender.setId(id);
        // get sender by id
        webTestClient.get()
                .uri(SENDER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Sender>() {
                })
                .isEqualTo(expectedSender);
    }

    @Test
    void canDeleteSender() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
        int age = RANDOM.nextInt(1, 100);

        SenderRegistrationRequest request = new SenderRegistrationRequest(name, email, age);

        // send a post request
        webTestClient.post()
                .uri(SENDER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), SenderRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all senders
        List<Sender> allSenders = webTestClient.get()
                .uri(SENDER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Sender>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = null;
        if (allSenders != null) {
            id = allSenders.stream()
                    .filter(sender -> sender.getEmail().equals(email))
                    .map(Sender::getId)
                    .findFirst()
                    .orElseThrow();
        }

        // delete sender
        webTestClient.delete()
                .uri(SENDER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get sender by id
        webTestClient.get()
                .uri(SENDER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateSender() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
        int age = RANDOM.nextInt(1, 100);

        SenderRegistrationRequest request = new SenderRegistrationRequest(name, email, age);

        // send a post request
        webTestClient.post()
                .uri(SENDER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), SenderRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all senders
        List<Sender> allSenders = webTestClient.get()
                .uri(SENDER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Sender>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = null;
        if (allSenders != null) {
            id = allSenders.stream()
                    .filter(sender -> sender.getEmail().equals(email))
                    .map(Sender::getId)
                    .findFirst()
                    .orElseThrow();
        }

        // update sender
        String newName = "newName";
        SenderUpdateRequest updateRequest = new SenderUpdateRequest(newName, null, null);
        webTestClient.put()
                .uri(SENDER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), SenderUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get sender by id
        Sender updatedSender = webTestClient.get()
                .uri(SENDER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Sender.class)
                .returnResult()
                .getResponseBody();

        Sender expected = new Sender(id, newName, email, age);

        assertThat(updatedSender).isEqualTo(expected);
    }
}
