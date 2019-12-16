package it.barusu.tutorial.dubbo.srvb;

import it.barusu.tutorial.dubbo.commons.ResteasyServiceBEndpoint;
import it.barusu.tutorial.dubbo.commons.RpcServiceCEndpoint;
import it.barusu.tutorial.dubbo.commons.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ResteasyServiceBEndpointImpl implements ResteasyServiceBEndpoint {

    private RpcServiceCEndpoint rpcServiceCEndpoint;

    @Autowired
    public ResteasyServiceBEndpointImpl(RpcServiceCEndpoint rpcServiceCEndpoint) {
        this.rpcServiceCEndpoint = rpcServiceCEndpoint;
    }

    @Override
    public Map<String, String> oops(@Valid UserRequest request) {
        log.info("resteasy request [oops]: {}", request);
        String word = rpcServiceCEndpoint.sayHello(request);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("word", "oops, everyone!");

        return responseMap;
    }

}
