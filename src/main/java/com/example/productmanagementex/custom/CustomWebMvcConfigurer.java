package com.example.productmanagementex.custom;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ルートディレクトリの絶対パスを指定
        String rootPath = System.getProperty("user.dir");
        String imagePath = "file:" + rootPath + "/uploaded-img/";
        registry.addResourceHandler("/uploaded-img/**").addResourceLocations(imagePath);
    }

}
