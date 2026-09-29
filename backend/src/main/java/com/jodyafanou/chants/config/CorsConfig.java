package com.jodyafanou.chants.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] origines;

    public CorsConfig(@Value("${app.cors.allowed-origins}") String[] origines) {
        this.origines = origines;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(origines)
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
