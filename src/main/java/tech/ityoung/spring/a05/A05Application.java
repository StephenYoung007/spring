package tech.ityoung.spring.a05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import tech.ityoung.spring.a05.beanfactorybeanpostprocessor.ComponentScanBeanFactoryPostProcessor;
import tech.ityoung.spring.a05.config.Config;

import java.io.IOException;

@Slf4j
public class A05Application {
    public static void main(String[] args) throws IOException {
        // clean container
        GenericApplicationContext context = new GenericApplicationContext();

        // only register config
        context.registerBean("config", Config.class);

        // add support for @ComponentScan @Import @ImportResource @Bean
//        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(ComponentScanBeanFactoryPostProcessor.class);

        // add import for @Mapper, also add some basic bean post processor like internalCommonAnnotationProcessor， internalAutowiredAnnotationProcessor etc..
//        context.registerBean(MapperScannerConfigurer.class, beanDefinition -> beanDefinition.getPropertyValues().addPropertyValue("basePackage", "tech.ityoung.spring.a05.mapper"));

        // initiate bean container
        context.refresh();

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            log.warn(beanDefinitionName);
        }

        context.close();
    }
}
