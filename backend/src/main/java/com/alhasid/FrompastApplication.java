package com.alhasid;

import com.alhasid.sender.Sender;
import com.alhasid.sender.SenderRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@SpringBootApplication
public class FrompastApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrompastApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(SenderRepository senderRepository) throws NoSuchAlgorithmException {
        Random random = SecureRandom.getInstanceStrong();
        return args -> {
            Faker faker = new Faker();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            Sender sender = new Sender(
                    firstName + " " + lastName,
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
                    random.nextInt(16, 99)
            );
            senderRepository.save(sender);
        };
    }
}
