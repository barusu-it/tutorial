package it.barusu.tutorial.dubbo.srva;

import it.barusu.tutorial.dubbo.commons.ResteasyServiceBEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.spring.RestClientProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Slf4j
@Configuration
public class ResteasyClientConfig {


    @Bean
    public RestClientProxyFactoryBean<ResteasyServiceBEndpoint> resteasyServiceBEndpointRestClientProxyFactoryBean(
            @Value("${base-url.service-b}") String url) {
        RestClientProxyFactoryBean<ResteasyServiceBEndpoint> restClientProxyFactoryBean = new RestClientProxyFactoryBean<>();
        restClientProxyFactoryBean.setBaseUri(URI.create(url));
        restClientProxyFactoryBean.setServiceInterface(ResteasyServiceBEndpoint.class);

        return restClientProxyFactoryBean;
    }

    @Bean
    public ResteasyServiceBEndpoint resteasyServiceBEndpointClient(
            RestClientProxyFactoryBean<ResteasyServiceBEndpoint> resteasyServiceBEndpointRestClientProxyFactoryBean)
            throws Exception {
        return resteasyServiceBEndpointRestClientProxyFactoryBean.getObject();

    }


}
