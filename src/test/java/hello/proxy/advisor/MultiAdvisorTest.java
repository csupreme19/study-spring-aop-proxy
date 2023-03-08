package hello.proxy.advisor;

import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

@Slf4j
public class MultiAdvisorTest {

    /**
     * 단점: ProxyFactory, Proxy를 여러번 생성해야함
     */
    @Test
    @DisplayName("여러 프록시")
    void multiAdvisorTest1() {

        // 프록시1 생성 proxy1 -> advisor1 -> target
        ServiceInterface target1 = new ServiceImpl();
        ProxyFactory proxyFactory1 = new ProxyFactory(target1);
        Advisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        proxyFactory1.addAdvisor(advisor1);
        ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        // 프록시2 생성 proxy2 -> advisor2 -> proxy1 -> advisor1 -> target
        ServiceInterface target2 = proxy1;
        ProxyFactory proxyFactory2 = new ProxyFactory(target2);
        Advisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        proxyFactory2.addAdvisor(advisor2);
        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

        proxy2.save();
    }

    /**
     * 실제 Spring AOP에서는 하나의 target에 하나의 proxy만 생성한다.
     * 나머지는 모두 여러 advisor를 등록하여 여러 포인트컷에 대응하는 여러 어드바이스를 실행한다.
     */
    @Test
    @DisplayName("여러 어드바이저")
    void multiAdvisorTest2() {

        // proxy -> advisor1 -> advisor2 -> target
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2()));
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1()));
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
    }

    private static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }

    private static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }
}
