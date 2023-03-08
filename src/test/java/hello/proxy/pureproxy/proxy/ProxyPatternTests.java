package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.decorator.code.Component;
import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import hello.proxy.pureproxy.proxy.code.Subject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ProxyPatternTests {

    @Test
    @DisplayName("프록시 없음")
    void noProxyTest() {
        Subject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);

        long startTimeMs = System.currentTimeMillis();
        client.execute();
        log.info("resultTime={}ms", System.currentTimeMillis() - startTimeMs);

        startTimeMs = System.currentTimeMillis();
        client.execute();
        log.info("resultTime={}ms", System.currentTimeMillis() - startTimeMs);

        startTimeMs = System.currentTimeMillis();
        client.execute();
        log.info("resultTime={}ms", System.currentTimeMillis() - startTimeMs);
    }

    /**
     * 프록시 패턴
     */
    @Test
    @DisplayName("프록시 캐시 적용")
    void proxyCacheTest() {
        Subject realSubject = new RealSubject();
        Subject proxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(proxy);

        long startTimeMs = System.currentTimeMillis();
        client.execute();
        log.info("resultTime={}ms", System.currentTimeMillis() - startTimeMs);

        startTimeMs = System.currentTimeMillis();
        client.execute();
        log.info("resultTime={}ms", System.currentTimeMillis() - startTimeMs);

        startTimeMs = System.currentTimeMillis();
        client.execute();
        log.info("resultTime={}ms", System.currentTimeMillis() - startTimeMs);
    }

}
