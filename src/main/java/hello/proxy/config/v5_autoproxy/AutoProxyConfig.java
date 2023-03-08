package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

    // AspectJAutoProxy에 의해 Advisor만 Bean으로 등록되어 있으면 조건에 맞는 프록시를 자동으로 생성한다.
    // AnnotationAwareAspectJAutoProxyCreator
//    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        // advice
        Advice advice = new LogTraceAdvice(logTrace);

        // advisor
        Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        return advisor;
    }

    @Bean
    public Advisor advisor2(LogTrace logTrace) {
        // pointcut
        // AspectJ 표현식을 사용한 Pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");

        // advice
        Advice advice = new LogTraceAdvice(logTrace);

        // advisor
        Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        return advisor;
    }
}
