package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeDecorator implements Component {

    private final Component component;

    public TimeDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTimeMs = System.currentTimeMillis();

        String result = component.operation();

        long endTimeMs = System.currentTimeMillis();
        log.info("TimeDecorator 종료 resultTime={}ms", endTimeMs - startTimeMs);
        return result;
    }
}
