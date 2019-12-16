package it.barusu.tutorial.dubbo.commons;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Activate(group = "consumer")
public class DubboClientTraceFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        context.setAttachment(TraceHelper.TRACE_ID, TraceHelper.getTraceId(
                context.getAttachment(TraceHelper.TRACE_ID)));

        log.info("trace[{}] client[{}] request: {}", TraceHelper.getTraceId(),
                invocation.getMethodName(), Arrays.toString(invocation.getArguments()));
        Result result = invoker.invoke(invocation);
        log.info("trace[{}] client[{}] response: {}", TraceHelper.getTraceId(),
                invocation.getMethodName(), result.toString());

        return result;

    }
}
