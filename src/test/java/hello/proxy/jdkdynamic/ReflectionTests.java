package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
public class ReflectionTests {

    @Test
    @DisplayName("리플렉션 없이 테스트")
    void noReflectionTest() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA();    // 호출하는 메서드만 다르다.
        log.info("result={}", result1);
        // 공통 로직1 종료

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB();    // 공통 로직이지만 메서드가 달라서 공통화가 어려움
        log.info("result={}", result2);
        // 공통 로직2 종료
    }

    @Test
    @DisplayName("람다로 해결하기")
    void lambdaTest() {
        HelloTemplate target = new HelloTemplate();

        log.info("start");
        String result1 = target.call(() -> {
            log.info("callA");
            return "A";
        });
        log.info("result={}", result1);

        log.info("start");
        String result2 = target.call(() -> {
            log.info("callB");
            return "B";
        });
        log.info("result={}", result2);

        log.info("start");
        Long result3 = target.call(() -> {
            log.info("callC");
            return 1234L;
        });
        log.info("result={}", result3);
    }

    @Test
    @DisplayName("리플렉션 테스트")
    void reflectionTest() throws Exception {
        // 클래스의 메타정보 획득 (내부 클래스: $)
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTests$Hello");
        Hello target = new Hello();

        // callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");

        log.info("start");
        // Hello 클래스의 callA라는 메서드를 target이라는 인스턴스에 대하여 호출
        Object result1 = methodCallA.invoke(target);
        log.info("result1={}", result1);

        // callB 메서드 정보
        Method methodCallB = classHello.getMethod("callB");

        log.info("start");
        // Hello 클래스의 callA라는 메서드를 target이라는 인스턴스에 대하여 호출
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}", result2);

    }

    @Test
    @DisplayName("리플렉션 테스트2")
    void reflectionTest2() throws Exception {
        // 클래스의 메타정보 획득 (내부 클래스: $)
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTests$Hello");
        Hello target = new Hello();

        // callA 메서드 호출
        dynamicCall(classHello.getMethod("callA"), target);

        // callB 메서드 호출
        dynamicCall(classHello.getMethod("callB"), target);
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }


    static class Hello {

        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }

    static class HelloTemplate {

        public <T> T call(HelloStrategy<T> strategy) {
            return strategy.call();
        }
    }

    interface HelloStrategy<T> {
        T call();
    }

}
