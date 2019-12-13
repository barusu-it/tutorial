package it.barusu.tutorial.dubbo.endpoint;

import it.barusu.tutorial.dubbo.ServiceBEndpoint;
import it.barusu.tutorial.dubbo.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/say")
public class ServiceAEndpoint {

    private ServiceBEndpoint serviceBEndpoint;

    @Autowired
    public ServiceAEndpoint(ServiceBEndpoint serviceBEndpoint) {
        this.serviceBEndpoint = serviceBEndpoint;
    }

    @RequestMapping(value = "/hi", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> hi(@RequestBody UserRequest request) {
        log.info("rest request [hi]: {}", request);
        String word = serviceBEndpoint.sayHi(request);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("word", word);

        return responseMap;
    }
}
