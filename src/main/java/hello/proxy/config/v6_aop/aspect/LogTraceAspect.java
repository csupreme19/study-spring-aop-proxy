package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
// Aspect는 AOP 관점 지향 프로그래밍 라이브러리인 AspectJ에서 제공
// AnnotationAwareAspectJAutoProxyCreator
// 1. @Aspect 애너테이션 클래스를 읽어 Advisor로 변환하여 저장한다.
// 2. Advisor를 기반으로 프록시를 생성한다.
@Aspect
@RequiredArgsConstructor
public class LogTraceAspect {

    private final LogTrace trace;

    // 실행 이전, 이후 모두 제어 가능
    @Around("execution(* hello.proxy.app..*(..))")  // PointCut
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        // Advice
        TraceStatus status = null;
        try {
//            Method method = joinPoint.getClass().getMethod();
//            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            String message = joinPoint.getSignature().toShortString();
            status = trace.begin(message);
            Object result = joinPoint.proceed();
            trace.end(status);
            return result;
        } catch(Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
