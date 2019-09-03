package it.barusu.tutorial.springbootsimple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class App {

    @GetMapping(path = "/json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Message json() {
        return new Message("Hello, World!");
    }
    @GetMapping(path = "/plaintext",
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String plaintext() {
        return "Hello, World!";
    }

    public static void main(String... args) {
        SpringApplication.run(App.class, args);
    }
}
