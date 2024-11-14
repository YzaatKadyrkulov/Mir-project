package kg.mir.Mirproject.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String[] LIST = {
            "http://localhost:5173",
            "http://mir-nori.ru",
            "https://ec2-3-127-65-10.eu-central-1.compute.amazonaws.com",
            "http://ec2-3-127-65-10.eu-central-1.compute.amazonaws.com:8080"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(LIST)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}