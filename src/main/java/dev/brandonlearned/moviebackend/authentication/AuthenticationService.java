package dev.brandonlearned.moviebackend.authentication;

import java.io.IOException; 
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.brandonlearned.moviebackend.config.JwtService;
import dev.brandonlearned.moviebackend.models.Role;
import dev.brandonlearned.moviebackend.models.UserEntity;
import dev.brandonlearned.moviebackend.repositories.UserRepository;
import dev.brandonlearned.moviebackend.token.Token;
import dev.brandonlearned.moviebackend.token.TokenRepository;
import dev.brandonlearned.moviebackend.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	//both register and authenticate can be used for login, only register makes a new user
	
	public AuthenticationResponse register(RegisterRequest request) {
		Optional<UserEntity> response = userRepository.findByUsername(request.getUsername());
		if(response.isPresent()) {
			return AuthenticationResponse.builder()
					.build();
		}
		
		//build userEntity and save it to MongoDB
		var user = UserEntity.builder()
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER)
				.build();		
				var savedUser = userRepository.insert(user);
				
		//generate tokens
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		//build token and save it to MongoDB
		saveUserToken(savedUser, jwtToken);
				
		//build a response and set it back to the controller
		return AuthenticationResponse.builder()
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.username(request.getUsername())
				.role(Role.USER)
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.build();
	}
	
	public AuthenticationResponse authenticate(AuthenticationRequest request){
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
					request.getUsername(), 
					request.getPassword()
			)
		);
		//if at this point, user is authenticated
		//grab user by username from MongoDB
		var user = userRepository.findByUsername(request.getUsername())
				.orElseThrow();
		//generate token
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		//revoke all current tokens
		revokeAllUserTokens(user);
		//build token and save it to MongoDB
		saveUserToken(user, jwtToken);
		return AuthenticationResponse.builder()
				.firstname(user.getFirstname())
				.lastname(user.getLastname())
				.username(user.getUsername())
				.role(user.getRole())
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.build();
	}
	
	private void revokeAllUserTokens(UserEntity user) {
		var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
		if(validUserTokens.isEmpty()) {
			return;
		}
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.deleteAll(validUserTokens);
	}
	
	private void saveUserToken(UserEntity user, String jwtToken) {
		var token = Token.builder()
				.user(user)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.revoked(false)
				.expired(false)
				.build();
				tokenRepository.insert(token);
	}

	public void refreshToken(
		HttpServletRequest request, 
		HttpServletResponse response
	) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String username;
		//ensured the authorization header is not null and starts with Bearer
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			return;
		}
		//sets the token substring of the header
		refreshToken = authHeader.substring(7); //7 because "Bearer " is 7 length
		//grabs the username from the sub of the token
		username = jwtService.extractUsername(refreshToken);
		
		//check if username is connected and already authenticated
		if(username != null) {
			//get user details from database
			var user = this.userRepository.findByUsername(username)
					.orElseThrow();
			//check if user is valid or not
			if(jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = AuthenticationResponse.builder()
						//add user
					.firstname(user.getFirstname())		
					.lastname(user.getLastname())
					.username(user.getUsername())
					.role(user.getRole())
					.accessToken(accessToken)
					.refreshToken(refreshToken)
					.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
	
}
