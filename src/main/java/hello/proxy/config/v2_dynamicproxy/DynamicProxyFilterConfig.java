package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceFilterHandler;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyFilterConfig {

    private static final String[] PATTERNS = {"nolog*", "order*", "save*"};

    @Bean
    LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

    @Bean
    OrderControllerV1 orderControllerV1() {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderServiceV1());
        return (OrderControllerV1)Proxy.newProxyInstance(orderController.getClass().getClassLoader(), new Class[]{OrderControllerV1.class}, new LogTraceFilterHandler(orderController, logTrace(), PATTERNS));
    }

    @Bean
    OrderServiceV1 orderServiceV1() {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepositoryV1());
        return (OrderServiceV1)Proxy.newProxyInstance(orderService.getClass().getClassLoader(), new Class[]{OrderServiceV1.class}, new LogTraceFilterHandler(orderService, logTrace(), PATTERNS));
    }

    @Bean
    OrderRepositoryV1 orderRepositoryV1() {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        return (OrderRepositoryV1)Proxy.newProxyInstance(orderRepository.getClass().getClassLoader(), new Class[]{OrderRepositoryV1.class}, new LogTraceFilterHandler(orderRepository, logTrace(), PATTERNS));
    }
}
