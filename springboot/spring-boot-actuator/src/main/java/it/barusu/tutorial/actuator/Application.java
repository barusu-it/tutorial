package it.barusu.tutorial.actuator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@SpringBootApplication
@Configuration
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    @RequestMapping("/say")
    public String say(@RequestBody String word) {
        return "Say " + word;
    }

    @RequestMapping("/sayForm")
    public String sayForm(@RequestParam("word") String word) {
        return "Say " + word;
    }

    @RequestMapping("/sayFormData")
    public String sayFormData(@RequestParam("file") MultipartFile file) throws IOException {
        return "Say " + IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);
    }


    @RequestMapping("/waiting")
    public String waiting() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000L);
                log.info("slept 1s.");
            } catch (InterruptedException e) {
                // ignore
            }
        }

        return "DOWN";
    }
}
