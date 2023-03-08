package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Spring의 ProxyFacotry를 사용할때는
 * aopalicance 패키지의 MethodInterceptor를 사용한다.
 * (CGLIB의 MethodInterceptor와 이름이 같음)
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeAdvice 실행");
        long startTimeMs = System.currentTimeMillis();

        // 프록시 팩토리를 생성할때 target 정보를 넘겨서 MethodInvocation은 target에 대한 정보를 가지고 있음
        Object result = invocation.proceed();   // 직접 호출할 필요 없이 target을 찾아서 실행해줌

        long endTimeMs = System.currentTimeMillis();
        log.info("TimeAdvice 종료 resultTime={}ms", endTimeMs - startTimeMs);
        return result;
    }
}
