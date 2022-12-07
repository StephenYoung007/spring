package tech.ityoung.spring.a05.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tech.ityoung.spring.a05.component.Bean1;
import tech.ityoung.spring.a05.component.Bean2;
import tech.ityoung.spring.a05.mapper.Mapper1;
import tech.ityoung.spring.a05.mapper.Mapper2;

import javax.sql.DataSource;

@Configuration
@ComponentScan("tech.ityoung.spring.a05.component")
public class Config {
    @Bean(initMethod = "init")
    public Bean1 bean1() {
        return new Bean1();
    }

    public Bean2 bean2() {
        return new Bean2();
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean(initMethod = "init")
    public DruidDataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/world");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("22222222");
        return druidDataSource;
    }

//    @Bean
    public MapperFactoryBean<Mapper1> mapper1(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<Mapper1> mapperFactoryBean = new MapperFactoryBean<>(Mapper1.class);
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
        return mapperFactoryBean;
    }

//    @Bean
    public MapperFactoryBean<Mapper2> mapper2(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<Mapper2> mapperFactoryBean = new MapperFactoryBean<>(Mapper2.class);
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
        return mapperFactoryBean;
    }
}
