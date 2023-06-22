package com.alhasid;

import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderRepository;
import com.alhasid.taker.Gender;
import com.alhasid.taker.Taker;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class FrompastApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrompastApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(
            SenderRepository senderRepository,
            PasswordEncoder passwordEncoder) throws NoSuchAlgorithmException {
        Random random = SecureRandom.getInstanceStrong();
        return args -> {
            Faker faker = new Faker();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            int age = random.nextInt(16, 99);
            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

            Sender sender = new Sender(faker.internet().emailAddress(), passwordEncoder.encode("" + random.nextDouble()));
            Taker taker = new Taker(
                    firstName + " " + lastName,
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
                    age,
                    gender,
                    sender);
            sender.setTakers(List.of(taker));
            senderRepository.save(sender);
        };
    }
}
