package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace trace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace trace) {
        super(null);    // 프록시 객체에서는 부모 기능을 사용안하므로 null로 생성
        this.target = target;
        this.trace = trace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderService.orderItem()");
            target.orderItem(itemId);   // target 호출
            trace.end(status);
        } catch(Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
