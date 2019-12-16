package it.barusu.tutorial.dubbo.commons;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/deliver")
public interface ResteasyServiceBEndpoint {

    @POST
    @Path("/oops")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, String> oops(@Valid UserRequest request);

}
