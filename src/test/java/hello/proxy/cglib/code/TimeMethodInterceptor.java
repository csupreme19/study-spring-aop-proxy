package hello.proxy.cglib.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
public class TimeMethodInterceptor implements MethodInterceptor {

    private final Object target;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("TimeMethodInterceptor 실행");
        long startTimeMs = System.currentTimeMillis();

        Object result = methodProxy.invoke(target, args);   // CGLIB에서는 method보다 methodProxy 사용 권장
//       Object result = method.invoke(target, args);

        long endTimeMs = System.currentTimeMillis();
        log.info("TimeMetodInterceptor 종료 resultTime={}ms", endTimeMs - startTimeMs);
        return result;
    }
}