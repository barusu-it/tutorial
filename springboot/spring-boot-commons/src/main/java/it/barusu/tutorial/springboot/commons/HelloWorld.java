package it.barusu.tutorial.springboot.commons;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorld {

    @GetMapping(path = "/json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Message json() {
        return new Message("Hello, World!");
    }

    @PostMapping(path = "/hello",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Message hello(@RequestBody User user) {
        return new Message("Hello, World! " + user.getName());
    }

    @GetMapping(path = "/plaintext",
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String plaintext() {
        return "Hello, World!";
    }

}
