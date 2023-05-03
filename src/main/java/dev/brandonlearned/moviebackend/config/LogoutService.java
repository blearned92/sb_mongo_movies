package dev.brandonlearned.moviebackend.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import dev.brandonlearned.moviebackend.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler{

	public final TokenRepository tokenRepository;
	
	@Override
	public void logout(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Authentication authentication
	) {
		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		//ensured the authorization header is not null and starts with Bearer
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			response.setStatus(400);
			return;
		}
		//sets the token substring of the header
		jwtToken = authHeader.substring(7); //7 because "Bearer " is 7 length
		var storedToken = tokenRepository.findByToken(jwtToken)
				.orElse(null); //or else return null
		if(storedToken != null) {
			tokenRepository.delete(storedToken);
		}
	}
}
