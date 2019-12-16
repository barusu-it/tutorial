package it.barusu.tutorial.dubbo.commons;

import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@Component
@ApplicationPath("/resteasy-api")
public class JaxrsApplication extends Application {
}
