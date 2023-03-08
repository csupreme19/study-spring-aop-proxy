package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class BeanPostProcessorTest {

    @Test
    public void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);

        // BeanPostProcessor에 의해 바꿔치기 된 B 객체가 리턴
        B b = applicationContext.getBean(B.class);
        assertThat(b).isNotNull();

        // BeanPostProcessor에 의해 A 객체는 등록되지 않으므로 예외
        assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
    }

    @Configuration
    static class BasicConfig {
        @Bean("beanA")
        public A a() {
            return new A();
        }

        @Bean("BeanPostProcessor")
        public AToBPostProcessor aToBPostProcessor() {
            return new AToBPostProcessor();
        }
    }

    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    static class AToBPostProcessor implements BeanPostProcessor {

        // @PostConstruct 이전
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        }

        // @PostConstruct 이후
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName={}, bean={}", beanName, bean);
            if(bean instanceof A) {
                return new B();
            }
            return bean;
        }

    }
}
