package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {

    private final OrderControllerV1 target;
    private final LogTrace trace;

    /**
     * 부가기능 프록시(데코레이터)
     */
    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            String result = target.request(itemId);
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
    public String noLog(String itemId) {
        return target.request(itemId);
    }
}
