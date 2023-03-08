package hello.proxy.jdkdynamic.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 동적 프록시 구현은 JDK Reflection의 InvocationHandler를 사용한다.
 */
@Slf4j
@RequiredArgsConstructor
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        log.info("TimeProxy 실행");
        long startTimeMs = System.currentTimeMillis();
        sleep(1000);
        Object result = method.invoke(target, objects);
        long endTimeMs = System.currentTimeMillis();
        log.info("TimeProxy 종료 resultTime={}ms", endTimeMs - startTimeMs);
        return result;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
