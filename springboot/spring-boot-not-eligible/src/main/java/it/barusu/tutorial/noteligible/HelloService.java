package it.barusu.tutorial.noteligible;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloService {

    @Async
    public String hello() {
        log.info("Local thread is {}", Thread.currentThread().getName());
        return "hello world.";
    }
}
