package com.dashaval.frompast;

import com.dashaval.frompast.sender.Sender;
import com.dashaval.frompast.sender.SenderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class FrompastApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrompastApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(SenderRepository senderRepository) {
        return args -> {
            Sender alex = new Sender("Alex", "alex@gamil.com", 32);
            Sender alim = new Sender("Alim", "alim@gamil.com", 9);
            Sender hava = new Sender("Hava", "hava@gamil.com", 7);

            senderRepository.saveAll(List.of(alex, alim, hava));
        };
    }
}
