package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
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
public class InterfaceProxyConfig {

    /**
     * Bean 생성자를 여러번 호출하여도 내부적으로 싱글톤 동작하기 때문에 생성자는 한번만 호출된다.
     * 이후 생성된 Bean을 재사용
     */
    @Bean
    LogTrace logTrace() {
        LogTrace logTrace = new ThreadLocalLogTrace();
        log.info("logTrace init={}", logTrace);
        return logTrace;
    }

    @Bean
    public OrderControllerV1 orderController() {
        OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService());

        LogTrace logTrace = logTrace();
        log.info("OrderControllerV1 init LogTrace init={}", logTrace);

        return new OrderControllerInterfaceProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderServiceV1 orderService() {
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository());

        LogTrace logTrace = logTrace();
        log.info("OrderServiceV1 init LogTrace init={}", logTrace);

        return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository() {
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();

        LogTrace logTrace = logTrace();
        log.info("OrderRepositoryV1 init LogTrace init={}", logTrace);

        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }

}
