package com.gerenciamento.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.stream.Stream;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-origin-patterns:}")
    private String allowedOriginPatterns;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // allowedOriginPatterns aceita tanto dominios exatos quanto curingas (ex: https://app-*.vercel.app)
        String[] origins = Stream.of(allowedOrigins, allowedOriginPatterns)
                .flatMap(lista -> Arrays.stream(lista.split(",")))
                .map(String::trim)
                .filter(origem -> !origem.isEmpty())
                .toArray(String[]::new);

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns(origins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
