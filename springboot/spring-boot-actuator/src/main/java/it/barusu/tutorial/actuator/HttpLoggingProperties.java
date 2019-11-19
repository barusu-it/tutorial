package it.barusu.tutorial.actuator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

import static it.barusu.tutorial.actuator.HttpLoggingFilter.DEFAULT_LENGTH_LIMIT;

@Getter
@Setter
@ConfigurationProperties(prefix = HttpLoggingProperties.PREFIX)
public class HttpLoggingProperties {

    public static final String PREFIX = "spring.http-logging";

    private boolean enabled = false;
    private boolean includeRequestPayload = false;
    private boolean includeResponsePayload = false;
    private boolean includeQueryString = false;
    private boolean includeClientInfo = false;
    private boolean includeHeaders = false;
    private int maxRequestPayloadLength = DEFAULT_LENGTH_LIMIT;
    private int maxResponsePayloadLength = DEFAULT_LENGTH_LIMIT;

    private Set<String> includedHeader = new HashSet<>();
    private Set<String> excludedHeader = new HashSet<>();

}
