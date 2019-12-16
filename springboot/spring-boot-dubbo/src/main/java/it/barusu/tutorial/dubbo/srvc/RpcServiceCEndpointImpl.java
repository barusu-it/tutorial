package it.barusu.tutorial.dubbo.srvc;

import it.barusu.tutorial.dubbo.commons.RpcServiceCEndpoint;
import it.barusu.tutorial.dubbo.commons.UserRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcServiceCEndpointImpl implements RpcServiceCEndpoint {
    @Override
    public String sayHello(UserRequest request) {
        log.info("dubbo rpc, say hello, {}", request.getName());
        return request.getName() + ", hello!";
    }
}
