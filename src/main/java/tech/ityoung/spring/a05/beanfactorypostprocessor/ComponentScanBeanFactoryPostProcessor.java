package tech.ityoung.spring.a05.beanfactorypostprocessor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import tech.ityoung.spring.a05.config.Config;

import java.io.IOException;

@Slf4j
public class ComponentScanBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);

            if (componentScan != null) {
                String[] basePackages = componentScan.basePackages();
                for (String basePackage : basePackages) {
                    log.warn(basePackage);
                    // tech.ityoung.a05.component -> classpath*:tech/ityoung/a05/component/**/*.class
                    String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                    log.error(path);

                    CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
//                    Resource[] resources = context.getResources(path);
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    for (Resource resource : resources) {
                        log.info(resource + "");
                        MetadataReader metadataReader = null;

                        metadataReader = readerFactory.getMetadataReader(resource);

                        // get the class name, just like tech.ityoung.spring.a05.component.Bean1
                        String className = metadataReader.getAnnotationMetadata().getClassName();
                        log.info(className);
                        // with @Component annotation
                        boolean withComponent = metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName());
                        log.warn("with @Component annotation " + withComponent);
                        // with annotation from @Component, like @Controller, @Service.
                        boolean fromComponent = metadataReader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName());
                        log.warn("with annotation from @Component " + fromComponent);

                        if (withComponent || fromComponent) {
                            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(className).getBeanDefinition();
                            if (configurableListableBeanFactory instanceof DefaultListableBeanFactory) {
                                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                                // generate bean name
                                String beanName = AnnotationBeanNameGenerator.INSTANCE.generateBeanName(beanDefinition, defaultListableBeanFactory);
                                defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
