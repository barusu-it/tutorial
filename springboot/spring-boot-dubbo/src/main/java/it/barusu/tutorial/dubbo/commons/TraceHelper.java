package it.barusu.tutorial.dubbo.commons;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class TraceHelper {
    public static String TRACE_ID = "traceId";

    private static ThreadLocal<String> traceIdThreadLocal = ThreadLocal.withInitial(() ->
            UUID.randomUUID().toString().replace("-", ""));

    public static String getTraceId(String invocationTraceId) {
        if (invocationTraceId != null) {
            traceIdThreadLocal.set(invocationTraceId);
        }
        return traceIdThreadLocal.get();
    }

    public static String getTraceId() {
        return traceIdThreadLocal.get();
    }

}
