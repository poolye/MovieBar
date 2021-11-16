package com.tec666.moviebar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: longge93
 * @Date: 2021/6/5 22:33
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {



    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String[] mapping = {"/api/**"};
        for (String item : mapping) {
            registry.addMapping(item)
                    .allowedOrigins("http://localhost:3001")
                    .allowedMethods("*")
                    .allowCredentials(true)
                    .maxAge(3600)
                    .allowedHeaders("*");
        }
    }

}
