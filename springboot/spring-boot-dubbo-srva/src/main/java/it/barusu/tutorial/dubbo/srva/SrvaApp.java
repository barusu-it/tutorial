package it.barusu.tutorial.dubbo.srva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"it.barusu.tutorial.dubbo.srva", "it.barusu.tutorial.dubbo.commons"})
@ImportResource(locations = "classpath:spring-dubbo.xml")
public class SrvaApp {

    public static void main(String... args) {
        SpringApplication.run(SrvaApp.class, args);
    }
}
