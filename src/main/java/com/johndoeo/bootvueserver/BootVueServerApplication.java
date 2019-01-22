package com.johndoeo.bootvueserver;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class BootVueServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootVueServerApplication.class, args);
    }

    /**
     * 配置数据源（dbcp2数据源）
     * @return
     */
    @Bean("dataSource")
    @ConfigurationProperties("datasource")
    public BasicDataSource getDataSource() {
        BasicDataSource source = new BasicDataSource();
        source.setPassword("123456");
        source.setUsername("root");
        source.setUrl("jdbc:mysql://localhost:3306/boot_vue_demo?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return source;
    }

    /**
     * 配置扫描dao数据访问层接口
     * @return
     */
    @Bean("bootVueServerScan")
    public MapperScannerConfigurer getMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.johndoeo.bootvueserver.dao");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return mapperScannerConfigurer;
    }

    /**
     * 配置扫描mybatis配置文件以及xml映射文件文件
     * @param basicDataSource
     * @return
     * @throws IOException
     */
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean getSqlSessionFactoryBean(BasicDataSource basicDataSource)throws IOException{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(basicDataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //只加载一个绝对匹配Resource,且通过ResourceLoader.getResource进行加载
        Resource[] resources = resolver.getResources("classpath:mybatis/mapper/*.xml");

        sqlSessionFactoryBean.setMapperLocations(resources);
        return sqlSessionFactoryBean;
    }

    /**
     * 初始化自定义的MyMapper接口,如果没有此bean的注入会在 访问数据库的时候 报错：
     *  java.lang.NoSuchMethodException: com.johndoeo.bootvueserver.mapper.provider.MyProvider.<init>()
     * @return
     */
    @Bean
    public tk.mybatis.spring.mapper.MapperScannerConfigurer getMapperConfigurer() {
        tk.mybatis.spring.mapper.MapperScannerConfigurer mapperScannerConfigurer = new tk.mybatis.spring.mapper.MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.isea533.mybatis.mapper");
        Properties p = new Properties();
        p.setProperty("mappers", "tk.mybatis.mapper.common.Mapper,com.johndoeo.bootvueserver.mapper.MyMapper");
        mapperScannerConfigurer.setProperties(p);
        return mapperScannerConfigurer;
    }

    /*@Bean
    public HttpMessageConverters fastJsonConfigure() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        converter.setFastJsonConfig(fastJsonConfig);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteEnumUsingToString);
        return new HttpMessageConverters(converter);
    }*/

}

