package it.barusu.tutorial.noteligible;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

// 在这里复现了 is not eligible 的场景，
// 如果实现的接口是 Ordered 而不是现在的 PriorityOrdered，就会在启动时提示 'is not eligible' 问题
@Slf4j
@Component
public class TestNotEligibleBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        HelloService helloService = applicationContext.getBean(HelloService.class);
        log.info("hello service: {}", helloService.getClass());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
