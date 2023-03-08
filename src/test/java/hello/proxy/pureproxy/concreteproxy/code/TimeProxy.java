package hello.proxy.pureproxy.concreteproxy.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimeProxy extends ConcreteLogic {

    private final ConcreteLogic target;

    @Override
    public String operation() {
        log.info("TimeProxy 실행");

        long startTimeMs = System.currentTimeMillis();
        String result = target.operation();
        long endTimeMs = System.currentTimeMillis();

        log.info("TimeProxy 종료 resultTime={}ms", endTimeMs - startTimeMs);
        return result;
    }



}
