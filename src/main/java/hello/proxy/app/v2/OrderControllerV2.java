package hello.proxy.app.v2;

import hello.proxy.app.v1.OrderControllerV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@ResponseBody
@RequestMapping
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;

    @GetMapping("/v2/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return itemId;
    }

    @GetMapping("/v2/no-log")
    public String noLog() {
        return "ok";
    }
}
