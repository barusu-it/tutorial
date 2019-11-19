package it.barusu.tutorial.actuator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import static it.barusu.tutorial.actuator.HttpLoggingProperties.PREFIX;
import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;

/**
 * Http logging configuration.
 *
 * @see org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceAutoConfiguration
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = PREFIX, name = "enabled")
@EnableConfigurationProperties(HttpLoggingProperties.class)
public class HttpLoggingAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = Type.SERVLET)
    static class ServletHttpLoggingConfiguration {

        @Bean
        @ConditionalOnMissingBean
        HttpLoggingFilter httpLoggingFilter(HttpLoggingProperties properties) {
            HttpLoggingFilter filter = new HttpLoggingFilter();

            filter.setIncludeClientInfo(properties.isIncludeClientInfo());
            filter.setIncludeHeaders(properties.isIncludeHeaders());
            filter.setIncludeQueryString(properties.isIncludeQueryString());
            filter.setIncludeRequestPayload(properties.isIncludeRequestPayload());
            filter.setIncludeResponsePayload(properties.isIncludeResponsePayload());
            filter.setMaxRequestPayloadLength(properties.getMaxRequestPayloadLength());
            filter.setMaxResponsePayloadLength(properties.getMaxResponsePayloadLength());

            filter.setHeaderPredicate(s ->
                    (properties.getIncludedHeader().size() == 0
                            || properties.getIncludedHeader().contains(s))
                            && (properties.getExcludedHeader().size() == 0
                            || !properties.getExcludedHeader().contains(s)));
            filter.setRequestLimit(request -> request.getHeader("Content-Type")
                    .contains(MediaType.MULTIPART_FORM_DATA_VALUE));
            filter.setResponseLimit(request -> request.getHeader("Content-Type")
                    .contains(MediaType.MULTIPART_FORM_DATA_VALUE));
            return filter;
        }
    }
}
