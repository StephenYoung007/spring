package tech.ityoung.spring.a05.beanfactorypostprocessor;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

public class MapperScannerBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        try {
            CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
            String path = "tech/ityoung/spring/a05/mapper/**/*.class";
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
            for (Resource resource : resources) {
                MetadataReader metadataReader = readerFactory.getMetadataReader(resource);
                ClassMetadata classMetadata = metadataReader.getClassMetadata();
                if (classMetadata.isInterface()) {
                    AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(MapperFactoryBean.class)
                            .addConstructorArgValue(classMetadata.getClassName())
                            .getBeanDefinition();
                    beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                    // only for generating the bean name, otherwise, the bean name should be
                    AbstractBeanDefinition nameBd = BeanDefinitionBuilder
                            .genericBeanDefinition(classMetadata.getClassName())
                            .getBeanDefinition();
                    String beanName = AnnotationBeanNameGenerator.INSTANCE.generateBeanName(nameBd, beanDefinitionRegistry);
                    beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
