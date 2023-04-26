package dev.brandonlearned.moviebackend.authentication;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.brandonlearned.moviebackend.config.JwtService;
import dev.brandonlearned.moviebackend.models.Role;
import dev.brandonlearned.moviebackend.models.UserEntity;
import dev.brandonlearned.moviebackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserRepository userRepository;
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
		var user = UserEntity.builder()
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER)
				.build();
		//add logic to check if user exists
		
		userRepository.insert(user);
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder()
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.username(request.getUsername())
				.role(Role.USER)
				.token(jwtToken)
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
		var user = userRepository.findByUsername(request.getUsername())
				.orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder()
				.firstname(user.getFirstname())
				.lastname(user.getLastname())
				.username(user.getUsername())
				.role(user.getRole())
				.token(jwtToken)
				.build();
	}
	
}
