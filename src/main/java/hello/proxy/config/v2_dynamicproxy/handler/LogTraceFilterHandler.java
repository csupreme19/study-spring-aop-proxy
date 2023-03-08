package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace trace;
    private final String[] patterns;

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//        if(Arrays.stream(patterns).anyMatch(pattern -> method.getName().contains(pattern))) {
//            log.info("match pattern={}, skip log", method.getName());
//            return method.invoke(target, args);
//        }

        // 메서드 이름 필터
        if(PatternMatchUtils.simpleMatch(patterns, method.getName())) {
            log.info("match pattern={}, skip log", method.getName());
            return method.invoke(target, args);
        }

        TraceStatus status = null;
        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = trace.begin(message);
            Object result = method.invoke(target, args);
            trace.end(status);
            return result;
        } catch(Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
