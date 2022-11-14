package com.uet.book_a_book.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        		.allowedMethods("*")
        		.allowedOrigins("*")
        		.allowedHeaders("*")
        		.allowCredentials(false)
        		.maxAge(-1);
    }
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
