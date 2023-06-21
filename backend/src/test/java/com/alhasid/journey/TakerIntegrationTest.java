package com.alhasid.journey;

import com.alhasid.sender.Sender;
import com.alhasid.taker.Gender;
import com.alhasid.taker.Taker;
import com.alhasid.taker.TakerRegistrationRequest;
import com.alhasid.taker.TakerUpdateRequest;
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
class TakerIntegrationTest {
//    private static final Random RANDOM = new Random();
//    private static final String TAKER_URI = "/api/v1/takers";
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Test
//    void canRegisterTaker() {
//        // create registration request
//        Faker faker = new Faker();
//        Name fakerName = faker.name();
//
//        String name = fakerName.fullName();
//        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
//        int age = RANDOM.nextInt(1, 100);
//        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
//        Sender sender = new Sender(faker.internet().emailAddress(), List.of(new Taker(name, email, age, gender)));
//
//        TakerRegistrationRequest request = new TakerRegistrationRequest(name, email, age, gender, sender);
//
//        // send a post request
//        webTestClient.post()
//                .uri(TAKER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), TakerRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        // get all takers
//        List<Taker> allTakers = webTestClient.get()
//                .uri(TAKER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Taker>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//        // make sure that taker is present
//        Taker expectedTaker = new Taker(name, email, age, gender);
//
//        assertThat(allTakers)
//                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
//                .contains(expectedTaker);
//
//        Long id = null;
//        if (allTakers != null) {
//            id = allTakers.stream()
//                    .filter(taker -> taker.getEmail().equals(email))
//                    .map(Taker::getId)
//                    .findFirst()
//                    .orElseThrow();
//        }
//
//        expectedTaker.setId(id);
//        // get taker by id
//        webTestClient.get()
//                .uri(TAKER_URI + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(new ParameterizedTypeReference<Taker>() {
//                })
//                .isEqualTo(expectedTaker);
//    }
//
//    @Test
//    void canDeleteTaker() {
//        // create registration request
//        Faker faker = new Faker();
//        Name fakerName = faker.name();
//
//        String name = fakerName.fullName();
//        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
//        int age = RANDOM.nextInt(1, 100);
//        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
//        Sender sender = new Sender(faker.internet().emailAddress(), List.of(new Taker(name, email, age, gender)));
//        sender.getTakers().forEach(taker -> taker.setSender(sender));
//
//        TakerRegistrationRequest request = new TakerRegistrationRequest(name, email, age, gender, sender);
//
//        // send a post request
//        webTestClient.post()
//                .uri(TAKER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), TakerRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        // get all takers
//        List<Taker> allTakers = webTestClient.get()
//                .uri(TAKER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Taker>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//        Long id = null;
//        if (allTakers != null) {
//            id = allTakers.stream()
//                    .filter(taker -> taker.getEmail().equals(email))
//                    .map(Taker::getId)
//                    .findFirst()
//                    .orElseThrow();
//        }
//
//        // delete taker
//        webTestClient.delete()
//                .uri(TAKER_URI + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        // get taker by id
//        webTestClient.get()
//                .uri(TAKER_URI + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isNotFound();
//    }
//
//    @Test
//    void canUpdateTaker() {
//        // create registration request
//        Faker faker = new Faker();
//        Name fakerName = faker.name();
//
//        String name = fakerName.fullName();
//        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "mail.ru";
//        int age = RANDOM.nextInt(1, 100);
//        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
//        Sender sender = new Sender(faker.internet().emailAddress(), List.of(new Taker(name, email, age, gender)));
//        sender.getTakers()
//                .forEach(taker -> taker.setSender(sender));
//
//        TakerRegistrationRequest request = new TakerRegistrationRequest(name, email, age, gender, sender);
//
//        // send a post request
//        webTestClient.post()
//                .uri(TAKER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), TakerRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        // get all takers
//        List<Taker> allTakers = webTestClient.get()
//                .uri(TAKER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Taker>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//        Long id = null;
//        if (allTakers != null) {
//            id = allTakers.stream()
//                    .filter(taker -> taker.getEmail().equals(email))
//                    .map(Taker::getId)
//                    .findFirst()
//                    .orElseThrow();
//        }
//
//        // update taker
//        String newName = "newName";
//        TakerUpdateRequest updateRequest = new TakerUpdateRequest(newName, null, null);
//        webTestClient.put()
//                .uri(TAKER_URI + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(updateRequest), TakerUpdateRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        // get taker by id
//        Taker updatedTaker = webTestClient.get()
//                .uri(TAKER_URI + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(Taker.class)
//                .returnResult()
//                .getResponseBody();
//
//        Taker expected = new Taker(id, newName, email, age, gender);
//
//        assertThat(updatedTaker).isEqualTo(expected);
//    }
}
