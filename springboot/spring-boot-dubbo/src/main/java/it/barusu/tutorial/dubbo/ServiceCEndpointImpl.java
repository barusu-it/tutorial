package it.barusu.tutorial.dubbo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceCEndpointImpl implements ServiceCEndpoint {
    @Override
    public String sayHello(UserRequest request) {
        log.info("dubbo rpc, say hello, {}", request.getName());
        return request.getName() + ", hello!";
    }
}
