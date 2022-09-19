package tech.ityoung.spring.a05;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import tech.ityoung.spring.a05.config.Config;

@Slf4j
public class A05Application {
    public static void main(String[] args) {
        // clean container
        GenericApplicationContext context = new GenericApplicationContext();

        // only register config
        context.registerBean("config", Config.class);

        // add support for @ComponentScan @Import @ImportResource @Bean
        context.registerBean(ConfigurationClassPostProcessor.class);

        // add import for @Mapper, also add some basic bean post processor like internalCommonAnnotationProcessor， internalAutowiredAnnotationProcessor etc..
        context.registerBean(MapperScannerConfigurer.class,
                beanDefinition -> beanDefinition.getPropertyValues().addPropertyValue("basePackage", "tech.ityoung.spring.a05.mapper"));


        context.refresh();

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            log.error(beanDefinitionName);
        }

        context.close();
    }
}
