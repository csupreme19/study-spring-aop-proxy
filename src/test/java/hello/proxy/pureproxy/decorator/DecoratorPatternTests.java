package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTests {

    @Test
    @DisplayName("프록시 없음")
    void noDecoratorTest() {
        Component realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);

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
     * 데코레이터 패턴
     */
    @Test
    @DisplayName("데코레이터 적용")
    void decoratorTest() {
        Component realComponent = new RealComponent();
        Component decorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(decorator);

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

    @Test
    @DisplayName("데코레이터 프록시 체인 적용")
    void decoratorProxyChainTest() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        Component timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
        client.execute();
        client.execute();
    }

}
