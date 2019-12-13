package it.barusu.tutorial.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ServiceBEndpointImpl implements ServiceBEndpoint {

    private ServiceCEndpoint serviceCEndpoint;

    @Autowired
    public ServiceBEndpointImpl(ServiceCEndpoint serviceCEndpoint) {
        this.serviceCEndpoint = serviceCEndpoint;
    }

    @Override
    public String sayHi(UserRequest request) {
        log.info("dubbo rpc, say hi, {}", request.getName());
        String hello = serviceCEndpoint.sayHello(request);
        return "Hi, " + hello;
    }
}
