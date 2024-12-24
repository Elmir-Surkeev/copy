package com.travelagency.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Для локальных статических ресурсов
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // Для внешних изображений
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}