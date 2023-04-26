package dev.brandonlearned.moviebackend.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			//contains the list of filters needed to execute
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {	
		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String username;
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwtToken = authHeader.substring(7); //7 because "Bearer " is 7 length
		username = jwtService.extractUsername(jwtToken);
		//check if username is connected and already authenticated
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			//get user details from database
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			//check if user is valid or not
			if(jwtService.isTokenValid(jwtToken, userDetails)) {
				//create object of this token passing userDetails, credentials, and authorities
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,
						null, //passing null because no credentials exist as of now
						userDetails.getAuthorities()
				);
				//enforce auth token with details of the request
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
				);
				//update the auth token
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		//pass to next filters to be executed
		filterChain.doFilter(request, response);
	}

}
