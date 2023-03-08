package hello.proxy.config.v4_postprocessor.postprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
@RequiredArgsConstructor
public class PackageLogTracePostProcessor implements BeanPostProcessor {

    private final String basePackage;
    private final Advisor advisor;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // basePackage에 해당하는 빈만 후처리
        String packageName = bean.getClass().getPackageName();
        if(!packageName.startsWith(basePackage)) {
            return bean;
        }
        log.info("param beanName={} bean={}", beanName, bean.getClass());

        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);

        Object proxy = proxyFactory.getProxy();
        log.info("create proxy={} target={}", proxy.getClass(), bean.getClass());
        return proxy;
    }

}
