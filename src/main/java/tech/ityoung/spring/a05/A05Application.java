package tech.ityoung.spring.a05;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
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

        // add import for @Mapper, also add some basic bean post processor like internalCommonAnnotationProcessor， internalAutowiredAnnotationProcessor etc..
//        context.registerBean(MapperScannerConfigurer.class, beanDefinition -> beanDefinition.getPropertyValues().addPropertyValue("basePackage", "tech.ityoung.spring.a05.mapper"));

        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);

        if (componentScan != null) {
            String[] basePackages = componentScan.basePackages();
            for (String basePackage : basePackages) {
                log.warn(basePackage);
                // tech.ityoung.a05.component -> classpath*:tech/ityoung/a05/component/**/*.class
                String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                log.error(path);

                CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
                Resource[] resources = context.getResources(path);
                for (Resource resource : resources) {
                    log.info(resource + "");
                    MetadataReader metadataReader = readerFactory.getMetadataReader(resource);
                    System.out.println(metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName()));
                }
            }
        }

        context.refresh();

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            log.warn(beanDefinitionName);
        }

        context.close();
    }
}
