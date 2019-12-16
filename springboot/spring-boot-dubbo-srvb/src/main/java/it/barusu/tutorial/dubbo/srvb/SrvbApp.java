package it.barusu.tutorial.dubbo.srvb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"it.barusu.tutorial.dubbo.srvb", "it.barusu.tutorial.dubbo.commons"})
@ImportResource(locations = "classpath:spring-dubbo.xml")
public class SrvbApp {

    public static void main(String... args) {
        SpringApplication.run(SrvbApp.class, args);
    }
}
