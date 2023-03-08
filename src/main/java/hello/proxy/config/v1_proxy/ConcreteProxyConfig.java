package hello.proxy.config.v1_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 실제 객체 대신 실체 객체를 참조하는 프록시 객체를 Bean으로 등록한다.
 */
@Slf4j
@Configuration
public class ConcreteProxyConfig {

    /**
     * Bean 생성자를 여러번 호출하여도 내부적으로 싱글톤 동작하기 때문에 생성자는 한번만 호출된다.
     * 이후 생성된 Bean을 재사용
     */
    @Bean
    LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

    @Bean
    public OrderControllerV2 orderController() {
        OrderControllerV2 controller = new OrderControllerV2(orderService());
        return new OrderControllerConcreteProxy(controller, logTrace());
    }

    @Bean
    public OrderServiceV2 orderService() {
        OrderServiceV2 service = new OrderServiceV2(orderRepository());
        return new OrderServiceConcreteProxy(service, logTrace());
    }

    @Bean
    public OrderRepositoryV2 orderRepository() {
        OrderRepositoryV2 repository = new OrderRepositoryV2();
        return new OrderRepositoryConcreteProxy(repository, logTrace());
    }

}
