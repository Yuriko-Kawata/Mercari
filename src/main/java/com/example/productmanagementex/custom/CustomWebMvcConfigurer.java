package com.example.productmanagementex.custom;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * imageアップロードの絶対パス指定
 * 
 * @author hiraizumi
 */
@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) { // ルートディレクトリの絶対パスを指定
        String rootPath = System.getProperty("user.dir");
        String imagePath = "file:" + rootPath + "/uploaded-img/";
        registry.addResourceHandler("/uploaded-img/**").addResourceLocations(imagePath);
    }

}
