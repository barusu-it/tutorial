package it.barusu.tutorial.dubbo.srva;

import it.barusu.tutorial.dubbo.commons.ResteasyServiceBEndpoint;
import it.barusu.tutorial.dubbo.commons.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Path("/say")
public class ResteasyServiceAEndpointImpl {

    @Resource
    @Qualifier("resteasyServiceBEndpointClient")
    private ResteasyServiceBEndpoint resteasyServiceBEndpointClient;

//    @Autowired
//    public ResteasyServiceAEndpointImpl(@Qualifier("resteasyServiceBEndpointClient")
//                                            ResteasyServiceBEndpoint resteasyServiceBEndpointClient) {
//        this.resteasyServiceBEndpointClient = resteasyServiceBEndpointClient;
//    }

    @POST
    @Path("/oops")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> oops(@Valid UserRequest request) {
        log.info("resteasy request [oops]: {}", request);
        resteasyServiceBEndpointClient.oops(request);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("word", "oops, everyone!");

        return responseMap;
    }

}
