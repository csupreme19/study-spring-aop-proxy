package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTests {

    @Test
    @DisplayName("JDK 동적 프록시 테스트")
    void jdkDynamicProxyTestA() {
        AInterface target = new AImpl();

        // 호출 핸들러 지정
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 프록시 객체를 동적으로 생성한다.
        AInterface proxy = (AInterface)Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);

        proxy.call();
        log.info("targetClass={}", target.getClass());  // hello.proxy.jdkdynamic.code.AImpl
        log.info("proxyClass={}", proxy.getClass());    // com.sun.proxy.$Proxy13
    }

    @Test
    @DisplayName("JDK 동적 프록시 테스트2")
    void jdkDynamicProxyTestB() {
        BInterface target = new BImpl();

        // 호출 핸들러 지정
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 프록시 객체를 동적으로 생성한다.
        BInterface proxy = (BInterface)Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call();
        log.info("targetClass={}", target.getClass());  // hello.proxy.jdkdynamic.code.BImpl
        log.info("proxyClass={}", proxy.getClass());    // com.sun.proxy.$Proxy13
    }
}
