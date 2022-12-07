package tech.ityoung.spring.a05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.GenericApplicationContext;
import tech.ityoung.spring.a05.beanfactorypostprocessor.BeanBeanFactoryPostProcessor;
import tech.ityoung.spring.a05.config.Config;

import java.io.IOException;

@Slf4j
public class AtBeanDemo {
    public static void main(String[] args) throws IOException {
        // clean container
        GenericApplicationContext context = new GenericApplicationContext();

        // only register config
        context.registerBean("config", Config.class);

        context.registerBean(BeanBeanFactoryPostProcessor.class);

        // initiate bean container
        context.refresh();

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            log.warn(beanDefinitionName);
        }

        context.close();
    }
}
