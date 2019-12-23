package it.barusu.tutorial.noteligible;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("hello")
public class HelloController {

    @Resource
    private HelloService helloService;

    @RequestMapping("say")
    public String say() {
        return helloService.hello();
    }
}
