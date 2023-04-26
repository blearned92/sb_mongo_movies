package dev.brandonlearned.moviebackend.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//this class describes the expected object to be passed in to the authentication endpoint
public class AuthenticationRequest {

	String username;
	String password;
}
