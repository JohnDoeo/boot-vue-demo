package com.johndoeo.bootvueserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Value("${file-path.absolutePath}")
    public String absolutePath;

    @Value("${file-path.face.fPath}")
    public String fPath;

    @Value("${file-path.image.iPath}")
    public String iPath;

    /*@Autowired
    private AuthorityAnnotationInterceptor authorityAnnotationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityAnnotationInterceptor);
    }*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/"+fPath+"**").addResourceLocations("file:" + absolutePath);
        registry.addResourceHandler("/"+iPath+"**").addResourceLocations("file:" + absolutePath);
        super.addResourceHandlers(registry);
    }
}