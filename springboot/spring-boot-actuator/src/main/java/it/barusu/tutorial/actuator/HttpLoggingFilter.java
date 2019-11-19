package it.barusu.tutorial.actuator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Predicate;

/**
 * Http logging filter for servlet.
 *
 * @see org.springframework.web.filter.ShallowEtagHeaderFilter
 * @see org.springframework.web.filter.AbstractRequestLoggingFilter
 * @see org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter
 */
@Slf4j
@Getter
@Setter
public class HttpLoggingFilter extends OncePerRequestFilter {

    public static final int DEFAULT_LENGTH_LIMIT = 1024;

    private Predicate<String> headerPredicate = (s) -> true;
    private Predicate<HttpServletRequest> requestLimit = (request) -> true;
    private Predicate<HttpServletRequest> responseLimit = (request) -> true;

    private boolean includeRequestPayload = false;
    private boolean includeResponsePayload = false;
    private int maxRequestPayloadLength = DEFAULT_LENGTH_LIMIT;
    private int maxResponsePayloadLength = DEFAULT_LENGTH_LIMIT;

    private boolean includeQueryString = false;
    private boolean includeClientInfo = false;
    private boolean includeHeaders = false;

    private ObjectMapper objectMapper = initObjectMapper();

    private ObjectMapper initObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModules(new JavaTimeModule(), new Jdk8Module());

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);

        HttpServletRequest requestToUse = request;
        HttpServletResponse responseToUse = response;

        if (isIncludeRequestPayload() && isFirstRequest && !(request instanceof HttpLoggingRequest)) {

            requestToUse = this.maxRequestPayloadLength > 0
                    ? new HttpLoggingRequest(request, getMaxRequestPayloadLength(), requestLimit.test(requestToUse))
                    : new HttpLoggingRequest(request);
        }

        if (isIncludeResponsePayload() && !(response instanceof HttpLoggingRequest)) {
            responseToUse = new HttpLoggingResponse(response);
        }

        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            if (!isAsyncStarted(requestToUse)) {

                try {
                    LoggingMessage message = createMessage(requestToUse, responseToUse);
                    log.info(objectMapper.writeValueAsString(message));
                } catch (JsonProcessingException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }

    }

    private LoggingMessage createMessage(HttpServletRequest request, HttpServletResponse response) {
        LoggingMessage message = new LoggingMessage();
        message.setMethod(request.getMethod());
        message.setRequestUri(request.getRequestURI());

        if (isIncludeQueryString()) {
            String queryString = request.getQueryString();
            if (!StringUtils.isEmpty(queryString)) {
                message.setRequestUri(message.getRequestUri() + "?" + queryString);
            }
        }

        if (isIncludeClientInfo()) {
            String client = request.getRemoteAddr();
            if (StringUtils.hasLength(client)) {
                message.setClient(client);
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                message.setSession(session.getId());
            }

            String user = request.getRemoteUser();
            if (user != null) {
                message.setUser(user);
            }
        }

        if (isIncludeHeaders()) {
            Enumeration<String> names = request.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                Enumeration<String> values = request.getHeaders(name);
                List<String> valueList = new ArrayList<>();
                while (values.hasMoreElements()) {
                    valueList.add(values.nextElement());
                }

                if (this.headerPredicate.test(name)) {
                    message.getHeaders().put(name, valueList.toString());
                }
            }
        }

        if (isIncludeRequestPayload()) {
            HttpLoggingRequest wrapper = WebUtils.getNativeRequest(request, HttpLoggingRequest.class);

            if (wrapper != null) {
                String payload;
                try {
                    payload = new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    payload = "[unknown]";
                }
                message.setRequestPayload(payload);
            }
        }

        if (isIncludeResponsePayload()) {
            HttpLoggingResponse wrapper = WebUtils.getNativeResponse(response, HttpLoggingResponse.class);

            if (wrapper != null) {
                String payload;
                try {
                    byte[] bytes = wrapper.getContentAsByteArray();
                    String encoding = wrapper.getCharacterEncoding();
                    payload = (maxResponsePayloadLength > 0 && bytes.length > maxResponsePayloadLength
                            && responseLimit.test(request))
                            ? new String(Arrays.copyOf(bytes, maxResponsePayloadLength), encoding) + "..."
                            : new String(bytes, encoding);
                    wrapper.copyBodyToResponse();
                } catch (IOException e) {
                    payload = "[unknown]";
                }
                message.setResponsePayload(payload);
            }
        }

        return message;
    }

    @Getter
    @Setter
    private static class LoggingMessage {

        private String method;
        private String requestUri;
        private String client;
        private String session;
        private String user;

        private Map<String, String> headers = new HashMap<>();

        private String requestPayload;
        private String responsePayload;
    }
}
