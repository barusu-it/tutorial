package it.barusu.tutorial.dubbo.srvb;

import it.barusu.tutorial.dubbo.commons.RpcServiceBEndpoint;
import it.barusu.tutorial.dubbo.commons.RpcServiceCEndpoint;
import it.barusu.tutorial.dubbo.commons.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class RpcServiceBEndpointImpl implements RpcServiceBEndpoint {

    private RpcServiceCEndpoint rpcServiceCEndpoint;

    @Autowired
    public RpcServiceBEndpointImpl(RpcServiceCEndpoint rpcServiceCEndpoint) {
        this.rpcServiceCEndpoint = rpcServiceCEndpoint;
    }

    @Override
    public String sayHi(UserRequest request) {
        log.info("dubbo rpc, say hi, {}", request.getName());
        String hello = rpcServiceCEndpoint.sayHello(request);
        return "Hi, " + hello;
    }
}
