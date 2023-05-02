package dev.brandonlearned.moviebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SbMongoMoviesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbMongoMoviesApplication.class, args);
	}
	
	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedOrigins(
					"http://localhost:3000",
					"https://localhost",
					"https://mongo-movies.netlify.app"
				)
				.allowedMethods("DELETE", "GET", "HEAD", "OPTIONS", "POST", "PUT")
				.allowCredentials(true);			
			}
		};
	}

}
