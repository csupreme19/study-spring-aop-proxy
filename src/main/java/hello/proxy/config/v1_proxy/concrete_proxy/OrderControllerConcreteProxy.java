package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderControllerConcreteProxy extends OrderControllerV2 {

    private final OrderControllerV2 target;
    private final LogTrace trace;

    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace trace) {
        super(null);    // 프록시 객체에서는 부모의 기능을 사용하지 않으므로 null로 생성
        this.target = target;
        this.trace = trace;
    }

    /**
     * 부가기능 프록시(데코레이터)
     */
    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            String result = target.request(itemId); // target 호출
            trace.end(status);
            return result;
        } catch(Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    /**
     * 접근제어 프록시
     */
    @Override
    public String noLog() {
        return target.noLog();
    }
}
