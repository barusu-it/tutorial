package it.barusu.tutorial.noteligible;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class HelloServiceTests {

    @Autowired
    private HelloService helloService;

    @Test
    public void testHello() throws InterruptedException {
        helloService.hello();
        Thread.sleep(3000L);
    }

}
