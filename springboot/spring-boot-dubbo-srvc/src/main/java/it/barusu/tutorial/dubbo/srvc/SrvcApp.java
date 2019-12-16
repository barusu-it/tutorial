package it.barusu.tutorial.dubbo.srvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"it.barusu.tutorial.dubbo.srvc", "it.barusu.tutorial.dubbo.commons"})
@ImportResource(locations = "classpath:spring-dubbo.xml")
public class SrvcApp {

    public static void main(String... args) {
        SpringApplication.run(SrvcApp.class, args);
    }
}
