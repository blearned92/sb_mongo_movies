package dev.brandonlearned.moviebackend.config;

import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutHandler logoutHandler;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.cors()
		.and()
		.csrf()
		.disable()
		.authorizeHttpRequests()
		.requestMatchers(
				"/api/v1/auth/**", 
				"/api/v1/movies/**",
				"api/v1/users"
		) //authorize all methods from these endpoints without token
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
		.logout()
		.logoutUrl("/api/v1/auth/logout") //customer logout path
		.addLogoutHandler(logoutHandler) //implement this handler whenever the above endpoint is called
		.logoutSuccessHandler(
			(request, response, authentication) -> 
			SecurityContextHolder.clearContext()
		);
		
		return http.build();
	}
	
}
