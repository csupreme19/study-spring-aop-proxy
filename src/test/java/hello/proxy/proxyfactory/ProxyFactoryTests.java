package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTests {

    @Test
    @DisplayName("인터페이스가 있을시 JDK Dynamic Proxy 생성")
    void interfaceProxyTest() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());   // Advice는 부가 기능 로직
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());    // JDK 동적 프록시($Proxy13)
        proxy.save();

        // AopUtils는 ProxyFactory를 이용하여 생성된 프록시만 검사 가능
        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // 프록시 팩토리를 사용하여 만든 프록시인지 확인
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue(); // JDK 동적 프록시인지 확인
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse(); // CGLIB 프록시인지 확인
    }

    @Test
    @DisplayName("구체 클래스만 있을시 CGLIB Proxy 생성")
    void concreteProxyTest() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());   // Advice는 부가 기능 로직
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());    // CGLIB 프록시($$EnhancerBySpringCGLIB)
        proxy.call();

        // AopUtils는 ProxyFactory를 이용하여 생성된 프록시만 검사 가능
        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // 프록시 팩토리를 사용하여 만든 프록시인지 확인
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse(); // JDK 동적 프록시인지 확인
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue(); // CGLIB 프록시인지 확인yyyyyyy
    }

    @Test
    @DisplayName("인터페이스가 있어도 ProxyTargetClass=true일때 CGLIB Proxy 생성")
    void proxyTargetClassTrueTest() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());   // Advice는 부가 기능 로직
        proxyFactory.setProxyTargetClass(true);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());    // JDK 동적 프록시($Proxy13)
        proxy.save();

        // AopUtils는 ProxyFactory를 이용하여 생성된 프록시만 검사 가능
        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // 프록시 팩토리를 사용하여 만든 프록시인지 확인
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse(); // JDK 동적 프록시인지 확인
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue(); // CGLIB 프록시인지 확인
    }

}