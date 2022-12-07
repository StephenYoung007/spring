package tech.ityoung.spring.a05.beanfactorypostprocessor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import tech.ityoung.spring.a05.config.Config;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Slf4j
public class BeanBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
            MetadataReader metadataReader = readerFactory.getMetadataReader(new ClassPathResource("tech/ityoung/spring/a05/config/Config.class"));
            Set<MethodMetadata> annotatedMethods = metadataReader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());

            for (MethodMetadata annotatedMethod : annotatedMethods) {
                String methodName = annotatedMethod.getMethodName();
                Map<String, Object> annotationAttributes = annotatedMethod.getAnnotationAttributes(Bean.class.getName());
                annotationAttributes.forEach((k, v) -> {
                    log.warn("method : {}  ---  key : {}, value : {}", methodName, k, v);
                });
                String initMethod = annotationAttributes.get("initMethod").toString();

                log.warn(methodName);
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();

                builder.setInitMethodName(initMethod);

                builder.setFactoryMethodOnBean(methodName, "config");

                // automatic assembly when bean created by factory method and constructor method.
                builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
                if (configurableListableBeanFactory instanceof DefaultListableBeanFactory) {
                    DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                    defaultListableBeanFactory.registerBeanDefinition(methodName, beanDefinition);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
